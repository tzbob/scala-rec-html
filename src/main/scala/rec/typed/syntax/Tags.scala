package rec.typed.syntax

import rec.typed.{Html, Tag}
import shapeless.HNil

object Tags extends Tags
trait Tags {
  val F = Fields
  val A = Attributes

  def text(str: String): Html["text", HNil] = Html(rec.Html.Text(str))

  type Sectioning = "header" with "section"
  type FormContent = "input"
  type Flow = "button" with "h1" with "div" with "p" with "text" with "ul" with FormContent with Sectioning

  type Labelable = "input" with "button"


  val dummy = Tag["dummy", "a", "f", "foo" with "bar"]("dummy")
  val label = Tag["label", A.ElementAttributes, F.ElementFields, Labelable with "text"]("label")

  val header = Tag["header", A.ElementAttributes, F.ElementFields, Flow]("header")
  val section = Tag["section", A.ElementAttributes, F.ElementFields, Flow]("section")

  val h1 = Tag["h1", A.ElementAttributes, F.ElementFields, Flow]("h1")

  val input = Tag["input", A.InputAttributes, F.InputFields, Flow]("input")

  val div = Tag["div", A.ElementAttributes, F.ElementFields, Flow]("div")
  val button = Tag["button", A.ElementAttributes, F.ElementFields, "text"]("button")

  val p = Tag["p", A.ElementAttributes, F.ElementFields, Flow]("p")

  val ul = Tag["ul", A.ElementAttributes, F.ElementFields, ListItems]("ul")

  type ListItems = "li"
  val li = Tag["li", A.ElementAttributes, F.ElementFields, Flow]("li")
}
