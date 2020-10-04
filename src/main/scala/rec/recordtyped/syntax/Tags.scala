package rec.recordtyped.syntax

import rec.Html
import rec.recordtyped.Tag
import shapeless.HNil

trait Tags extends Fields with Attributes {
  def text(str: String): Html[HNil] = rec.Html.Text(str)

  trait Text {
    lazy val text = Tags.this.text _
  }

  trait Sectioning {
    lazy val header  = Tags.this.header
    lazy val section = Tags.this.section
  }

  trait FormContent {
    lazy val input = Tags.this.input
  }

  trait Flow extends FormContent with Sectioning {
    lazy val button = Tags.this.button
    lazy val h1     = Tags.this.h1
    lazy val div    = Tags.this.div
    lazy val text   = Tags.this.text _
    lazy val ul     = Tags.this.ul
  }

  trait Labelable {
    lazy val input  = Tags.this.input
    lazy val button = Tags.this.button
  }

  trait ListItems {
    lazy val li = Tags.this.li
  }

  lazy val label =
    Tag("label",
        new ElementAttributes   {},
        new ElementFields       {},
        new Labelable with Text {})

  lazy val header =
    Tag("header", new ElementFields {}, new ElementFields {}, new Flow {})
  lazy val section =
    Tag("section", new ElementFields {}, new ElementFields {}, new Flow {})

  lazy val h1 =
    Tag("h1", new ElementFields {}, new ElementFields {}, new Flow {})

  lazy val input =
    Tag("input", new InputAttributes {}, new InputFields {}, new Flow {})

  lazy val div =
    Tag("div", new ElementAttributes {}, new ElementFields {}, new Flow {})
  lazy val button =
    Tag("button", new ElementAttributes {}, new ElementFields {}, new Text {})

  lazy val ul =
    Tag("ul", new ElementAttributes {}, new ElementFields {}, new ListItems {})

  lazy val li =
    Tag("li", new ElementAttributes {}, new ElementFields {}, new Flow {})
}

object Tags extends Tags
