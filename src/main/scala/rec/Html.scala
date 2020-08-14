package rec

import org.scalajs.dom
import rec.NodeList.NodeList
import rec.ReadList.ReadList
import rec.syntax.EventBindMaker
import shapeless.{HList, HNil}
import snabbdom.VNode

import scala.concurrent.duration.Duration
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.{Dictionary, |}

sealed trait Html[-Read]
object Html {
  case class Field[Read](key: String, fieldName: String) {
    private[Html] var currentElement: Option[() => dom.Element] = None
    private[Html] def read(): Read =
      currentElement
        .get()
        .asInstanceOf[js.Dynamic]
        .selectDynamic(key)
        .asInstanceOf[Read]
  }

  case class Element[R1 <: HList, R2 <: HList, Reads <: HList](
      tag: String,
      attributes: Seq[Attr],
      reads: ReadList[R1],
      children: NodeList[R2])(
      implicit recordConcat: RecordConcat[R1, R2, Reads])
      extends Html[Reads]

  case class Text(str: String) extends Html[HNil]

  case class SetTimeout[R](now: Html[R],
                           duration: Duration,
                           next: () => Html[R])
      extends Html[R]

  def ignore[R](html: Html[R]): Html[HNil] = html.asInstanceOf[Html[HNil]]

  def after[R](now: Html[R], duration: Duration, next: => Html[R]) =
    SetTimeout(now, duration, () => next)

  def fix[R <: HList](
      f: (EventBindMaker => ((R, dom.Event) => Html[_]) => Attr) => Html[R])(
      implicit fromMap: FromMap[R]): Html[HNil] = {

    lazy val readR = () => { // Try and figure out a way to properly use FromMap
      val fieldList = Html.allFields(htmlResult)
      val fieldValueList = fieldList.map { field =>
        field.fieldName -> field.read()
      }
      val fieldMap = fieldValueList.toMap
      fromMap(fieldMap) match {
        case None =>
          throw new RuntimeException(
            "Your read data does not match the JavaScript types")
        case Some(r) => r
      }
    }

    lazy val attrMaker
      : EventBindMaker => ((R, dom.Event) => Html[_]) => Attr = {
      (event: EventBindMaker) => (usedReaderF: (R, dom.Event) => Html[_]) =>
        Attr.FixedEventBind(event.key, (r: R, ev) => {
          usedReaderF(r, ev)
        }, readR)
    }

    lazy val htmlResult = f(attrMaker)
    ignore(htmlResult)
  }

  def allFields(html: Html[_]): List[Field[_]] = html match {
    case SetTimeout(now, _, _) => allFields(now)
    case Element(_, _, reads, children) =>
      reads.toList ::: children.toList.map(allFields).flatten
    case Text(_) => Nil
  }

  def toVNode[R <: HNil](html: Html[R], render: VNode => Unit): VNode =
    html match {
      case SetTimeout(now, duration, next) =>
        dom.window
          .setTimeout(() => render(toVNode(next(), render)), duration.toMillis)
        toVNode(now, render)
      case Html.Text(str) => str.asInstanceOf[VNode]
      case Html.Element(tag, attributes, readList, children) =>
        val jsAttributes: Dictionary[String] = attributes
          .collect {
            case Attr.Attribute(key, value) => key -> value
          }
          .toMap
          .toJSDictionary

        val jsProperties: Dictionary[String | Boolean | Int] = attributes
          .collect {
            case Attr.Property(key, value) => key -> value
          }
          .toMap
          .toJSDictionary

        val jsBindingsMap = attributes.collect {
          case Attr.EventBind(event, f) =>
            event -> ({ (ev: dom.Event) =>
              render(toVNode(f(ev), render))
            }: js.Function1[dom.Event, Unit])
          case Attr.FixedEventBind(event,
                                   f: ((R, dom.Event) => Html[_]),
                                   readR) =>
            event -> ({ (ev: dom.Event) =>
              val r = readR()
              f(r, ev)
              render(toVNode(f(r, ev), render))
            }: js.Function1[dom.Event, Unit])
        }.toMap
        val jsBindings: Dictionary[js.Function1[dom.Event, Unit]] =
          jsBindingsMap.toJSDictionary

        val jsChildren =
          children.toList.map(toVNode(_, render)).toJSArray

        val data = js.Dynamic.literal(
          attrs = jsAttributes,
          props = jsProperties,
          on = jsBindings
        )

        val vnode = snabbdom.h(tag, data, jsChildren)
        readList.toList.foreach { field =>
          field.currentElement = Some(() => vnode.elm.asInstanceOf[dom.Element])
        }
        vnode
    }
}
