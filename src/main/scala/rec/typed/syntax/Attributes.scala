package rec.typed.syntax

import rec.typed.Attribute

object Attributes extends Attributes
trait Attributes {
  import Attribute._

  type MouseEventHandlers = "click" with "dblclick"
  val onclick    = event("click")
  val ondblclick = event("dblclick")

  type ElementAttributes = "class" with "id" with MouseEventHandlers
  val `class` = attribute("class")
  val id      = attribute("id")

  type InputAttributes = "checked" with "type" with ElementAttributes
  val checked = property("checked")
  val `type`  = attribute("type")
}
