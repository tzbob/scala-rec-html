package rec

import rec.syntax.DSL._
import shapeless._
import shapeless.record._

import scala.language.reflectiveCalls

class ViewPerson extends RecHtmlApp {
  type Person = ("last" ->> String) :: ("first" ->> String) :: HNil

  val lastname =
    input(tpe("text"), field("value", "last".is[String]), NNil)
  val firstname =
    input(tpe("text"), field("value", "first".is[String]), NNil)

  val personInput = div(Nil, RNil, lastname :: firstname)

  def showPerson(person: Person): Html[_] = {
    val back = button(List(onclick(_ => inputPerson)), RNil, text("Back"))
    div(Nil,
        RNil,
        text(s"Person: ${person.apply("last")} ${person("first")}") :: back)
  }

  val inputPerson = Html.fix[Person] { bindWithReader =>
    val clickBind = bindWithReader(onclick) { (person, _) =>
      showPerson(person)
    }

    val btn = button(clickBind, RNil, text("Submit"))
    div(Nil, RNil, personInput :: btn)
  }

  val html = inputPerson
}
