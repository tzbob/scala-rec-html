package rec

import rec.syntax.DSL._
import shapeless._
import shapeless.record._

import scala.language.reflectiveCalls

class ViewPerson extends RecHtmlApp {
  type Person = ("last" ->> String) :: ("first" ->> String) :: HNil

  val lastname  = input(tpe("text"), field("value", "last".is[String]))
  val firstname = input(tpe("text"), field("value", "first".is[String]))

  val personInput = div(lastname :: firstname)

  def showPerson(person: Person): Html[_] = {
    val back = button(List(onclick(_ => inputPerson)), RNil, text("Back"))
    div(text(s"Person: ${person.apply("last")} ${person("first")}") :: back)
  }

  val inputPerson = Html.fix[Person] { bindWithReader =>
    val clickBind = bindWithReader(onclick) { (person, _) =>
      showPerson(person)
    }

    val btn = button(clickBind, RNil, text("Submit"))
    div(personInput :: btn)
  }

  val html = inputPerson
}
