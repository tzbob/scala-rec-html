package rec.recordtyped.syntax

import rec.syntax.EventBindMaker

trait Attributes {

  trait MouseEventHandlers {
    val onclick    = new EventBindMaker("click")
    val ondblclick = new EventBindMaker("dblclick")
  }

  trait ElementAttributes extends MouseEventHandlers {
    val `class` = rec.Attr.Attribute("class", _)
    val id      = rec.Attr.Attribute("id", _)
  }

  trait InputAttributes extends ElementAttributes {
    val checked = rec.Attr.Property("checked", _)
    val `type`  = rec.Attr.Attribute("type", _)
  }

}

object Attributes extends Attributes
