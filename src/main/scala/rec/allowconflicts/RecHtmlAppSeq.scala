package rec.allowconflicts

import org.scalajs.dom
import rec.isolate._
import snabbdom.VNode

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.{Dictionary, |}

trait RecHtmlAppSeq extends rec.isolate.RecHtmlApp {
  override def toReadableVNode[Use](html: Html[_, _],
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

        def newRead(): Map[String, Seq[Any]] = {
          val childReads: Seq[() => Map[String, Any]] = children.map(_.read)
          val fields                                  = aList.collect { case f @ Attribute.Field(_, _) => f }
          val fieldReads = fields.foldLeft(Map.empty[String, Seq[Any]]) { (map, field) =>
            // FIXME: temporary hack, assume all values are sequences
            // Implements R U+ R
            val original = map.getOrElse(field.fieldName, Seq.empty)
            val newValue = Seq(
              vnode.elm
                .asInstanceOf[js.Dynamic]
                .selectDynamic(field.key))
            map + (field.fieldName -> (original ++ newValue))
          }

          childReads.foldLeft(fieldReads) { (map, reader) =>
            val read = reader().asInstanceOf[Map[String, Seq[Any]]]
            (map.keys ++ read.keys).map { k =>
              k -> (map.getOrElse(k, Seq.empty) ++ read.getOrElse(k, Seq.empty))
            }.toMap
          }
        }

        newReader = newRead _

        List(ReadableVNode(vnode, newRead))
    }
}
