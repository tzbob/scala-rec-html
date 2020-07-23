package rec

import org.scalajs.dom
import rec.Html.Field

import scala.scalajs.js.|

trait Attr

object Attr {
  case class Attribute(key: String, value: String)                extends Attr
  case class Property(key: String, value: String | Boolean | Int) extends Attr

  case class EventBind(event: String, f: dom.Event => Html[_]) extends Attr
  case class FixedEventBind[R](event: String,
                               f: (R, dom.Event) => Html[_],
                               getFieldList: () => List[Field[_]])
      extends Attr
}
