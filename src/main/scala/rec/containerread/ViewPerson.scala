package rec.containerread

import rec.containerread.syntax.DSL._
import shapeless._
import shapeless.record._

import scala.language.reflectiveCalls

class ViewPerson extends RecHtmlApp {
  type Person = ("last" ->> String) :: ("first" ->> String) :: HNil

  val lastname  = input(tpe("text"), field("value", "last".is[String]))
  val firstname = input(tpe("text"), field("value", "first".is[String]))

  val personInput = div(lastname |+| firstname)

  def showPerson(person: Person): Html[Nothing, HNil] = Html.isolate {
    val back = button(onclick.as(inputPerson), text("Back"))
    div(text(s"Person: ${person.apply("last")} ${person("first")}") |+| back)
  }

  val inputPerson = Html.isolate {
    val btn = button(onclick.read(showPerson), text("Submit"))
    div(personInput |+| btn)
  }

  val main = inputPerson
}
