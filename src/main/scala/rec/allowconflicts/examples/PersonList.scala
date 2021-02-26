package rec.allowconflicts.examples

import rec.allowconflicts.RecHtmlAppSeq
import rec.allowconflicts.syntax.DSL._
import rec.isolate.Html
import shapeless._
import shapeless.record._

object PersonList extends RecHtmlAppSeq {
  type Names = ("names" as Seq[String]) :: HNil

  val nameInput = input(tpe("text") |+| field("value", "names".is[String]))

  def showAllNames(namesRaw: Names): Html[Nothing, HNil] = Html.html {
    val names = namesRaw("names")
    names.foldLeft(Html.empty) { (html, str) =>
      html |+| div(text("Input: ") |+| text(str))
    }
  }

  val nameInputs: Html[Names, Names] = {
    val btn = button(onclick.read(showAllNames), text("Submit"))
    val inputs: Html[Nothing, Names] = (1 to 10).foldLeft(nameInput) { (html, _) =>
      div(html) |+| nameInput
    }
    div(inputs |+| btn)
  }

  val main = Html.html(nameInputs)
}
