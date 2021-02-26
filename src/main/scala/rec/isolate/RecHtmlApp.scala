package rec.isolate

import org.scalajs.dom
import rec.DomPatcher
import shapeless.HNil
import snabbdom.{VNode, h}

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.{Dictionary, |}

trait RecHtmlApp {
  case class ReadableVNode(vnode: VNode, read: () => Map[String, Any])

  def toTopVNode(readableVNode: List[ReadableVNode]) = readableVNode match {
    case List(el) =>
      // Ugly hack to see if vnode is a string, top-level strings are not supported in snabbdom
      if (el.vnode.toString == el.vnode)
        h("span", js.Dynamic.literal(), List(el.vnode).toJSArray)
      else el.vnode
    case list => h("div", js.Dynamic.literal(), list.map(_.vnode).toJSArray)
  }

  def toReadableVNode[Use](html: Html[_, _],
                           doReadOpt: Option[() => Map[String, Any]],
                           render: List[ReadableVNode] => Unit): List[ReadableVNode] =
    html match {
      case Html.Nil => List.empty
      case Html.Combine(left, right) =>
        var newReader: () => Map[String, Any] = null
        val reader = doReadOpt match {
          case Some(value) => value
          case None =>
            () =>
              newReader()
        }
        val doRead = Some(reader)
        val newReadables =
          toReadableVNode(left, doRead, render) ::: toReadableVNode(right, doRead, render)
        newReader = () => newReadables.flatMap(_.read()).toMap
        newReadables
      case Html.Contained(html) => toReadableVNode(html, None, render)

      case Html.Text(str) =>
        List(ReadableVNode(str.asInstanceOf[VNode], () => Map.empty))

      case Html.RequestAnimationFrame(after) =>
        dom.window.requestAnimationFrame { _ =>
          render(toReadableVNode(after(), doReadOpt, render))
        }
        List.empty

      case Html.Element(tag, attribute, child) =>
        var newReader: () => Map[String, Any] = null
        // If there is no reader, 'newReader' is the new topmost reader
        val reader = doReadOpt match {
          case Some(value) => value
          case None =>
            () =>
              newReader()
        }

        val doRead = Some(reader)

        val aList = Attribute.list(attribute)
        val jsAttributes: Dictionary[String] =
          aList
            .collect {
              case Attribute.AttributeImpl(key, value) => key -> value
            }
            .toMap
            .toJSDictionary

        val jsProperties: Dictionary[String | Boolean | Int] = aList
          .collect {
            case Attribute.Property(key, value) => key -> value
          }
          .toMap
          .toJSDictionary

        val children   = toReadableVNode(child, doRead, render)
        val jsChildren = children.map(_.vnode).toJSArray

        val jsBindingsMap = aList.collect {
          case Attribute.EventBind(ev, f) =>
            ev -> ({ (ev: dom.Event) =>
              render(toReadableVNode(f(ev), doRead, render))
            }: js.Function1[dom.Event, Unit])

          case b @ Attribute.EventReadBind(ev, _) =>
            ev -> ({ (ev: dom.Event) =>
              val freshHtml = b.execF(ev, reader())
              render(toReadableVNode(freshHtml, doRead, render))
            }: js.Function1[dom.Event, Unit])
        }.toMap

        val jsBindings: Dictionary[js.Function1[dom.Event, Unit]] =
          jsBindingsMap.toJSDictionary

        val data = js.Dynamic.literal(
          attrs = jsAttributes,
          props = jsProperties,
          on = jsBindings
        )

        val vnode = snabbdom.h(tag, data, jsChildren)

        def newRead(): Map[String, Any] = {
          val childReads = children.map(_.read)
          val fields     = aList.collect { case f @ Attribute.Field(_, _) => f }
          val fieldReads = fields.foldLeft(Map.empty[String, Any]) { (map, field) =>
            map + (field.fieldName -> vnode.elm
              .asInstanceOf[js.Dynamic]
              .selectDynamic(field.key))
          }

          childReads.foldLeft(fieldReads) { (map, reader) =>
            map ++ reader()
          }
        }
        newReader = newRead _

        List(ReadableVNode(vnode, newRead))
    }

  def run(html: Html[_, _], element: dom.Element): Unit = {
    lazy val domPatcher: DomPatcher =
      new DomPatcher(
        toTopVNode(toReadableVNode(html, None, vn => domPatcher.applyNewState(toTopVNode(vn)))),
        element)
    domPatcher
    ()
  }

  val main: Html[Nothing, HNil]

  def main(args: Array[String]): Unit = {
    run(main, dom.document.getElementById("app"))
    println("Starting...")
  }
}
