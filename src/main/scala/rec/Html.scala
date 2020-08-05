package rec

import org.scalajs.dom
import rec.syntax.EventBindMaker
import snabbdom.VNode

import scala.concurrent.duration.Duration
import scala.scalajs.js
import scala.scalajs.js.{Dictionary, |}
import scala.scalajs.js.JSConverters._

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

  case class Element[-Reads](tag: String,
                             attributes: Seq[Attr],
                             reads: ReadList[Reads],
                             children: NodeList[Reads])
      extends Html[Reads]

  case class Text(str: String) extends Html[{}]

  case class SetTimeout[R](now: Html[R],
                           duration: Duration,
                           next: () => Html[R])
      extends Html[R]

  def ignore[R](html: Html[R]): Html[{}] = html.asInstanceOf[Html[{}]]

  def after[R](now: Html[R], duration: Duration, next: => Html[R]) =
    SetTimeout(now, duration, () => next)

  def fix[R](
      f: (EventBindMaker => ((R, dom.Event) => Html[_]) => Attr) => Html[R])
    : Html[_] = {

    lazy val attrMaker
      : EventBindMaker => ((R, dom.Event) => Html[_]) => Attr = {
      (event: EventBindMaker) => (usedReaderF: (R, dom.Event) => Html[_]) =>
        Attr.FixedEventBind(event.key,
                            usedReaderF,
                            () => Html.allFields(htmlResult))
    }
    lazy val htmlResult = f(attrMaker)
    htmlResult
  }

  def allFields[R](html: Html[R]): List[Field[_]] = html match {
    case SetTimeout(now, _, _) => allFields(now)
    case Element(_, _, reads, children) =>
      ReadList
        .toList(reads) ::: NodeList.toList(children).map(allFields).flatten
    case Text(_) => Nil
  }

  private def readListToR[R](fieldList: List[Field[_]], el: dom.Element): R = {
    val fieldValueList = fieldList.map { field =>
      field.fieldName -> field.read()
    }
    fieldValueList.toMap.toJSDictionary.asInstanceOf[R]
  }

  def toVNode[R](html: Html[R], render: VNode => Unit): VNode = html match {
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

      val jsBindings: Dictionary[js.Function1[dom.Event, Unit]] =
        attributes
          .collect {
            case Attr.EventBind(event, f) =>
              event -> ({ (ev: dom.Event) =>
                render(toVNode(f(ev), render))
              }: js.Function1[dom.Event, Unit])
            case Attr.FixedEventBind(event,
                                     f: ((R, dom.Event) => Html[_]),
                                     getFieldList) =>
              event -> ({ (ev: dom.Event) =>
                val r =
                  readListToR[R](getFieldList(),
                                 ev.currentTarget.asInstanceOf[dom.Element])
                f(r, ev)
                render(toVNode(f(r, ev), render))
              }: js.Function1[dom.Event, Unit])
          }
          .toMap
          .toJSDictionary

      val jsChildren =
        NodeList.toList(children).map(toVNode(_, render)).toJSArray

      val data = js.Dynamic.literal(
        attrs = jsAttributes,
        props = jsProperties,
        on = jsBindings
      )

      val vnode = snabbdom.h(tag, data, jsChildren)
      ReadList.toList(readList).foreach { field =>
        field.currentElement = Some(() => vnode.elm.asInstanceOf[dom.Element])
      }
      vnode
  }
}
