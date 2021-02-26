package rec.isolate.examples

import rec.isolate.syntax.DSL._
import rec.isolate.{Html, RecHtmlApp}
import shapeless._
import shapeless.record._

class Wizard extends RecHtmlApp {
  case class Person(given: String, family: String, age: Int)
  type Names = ("last" as String) :: ("first" as String) :: HNil

  val lastname  = input(tpe("text") |+| F.value("last"))
  val firstname = input(tpe("text") |+| F.value("first"))
  val age       = input(tpe("number") |+| F.value("age"))

  def page1(nextPage: Names => Html.SHtml) = Html.html {
    lastname |+| firstname |+| button(onclick.read(nextPage), text("Next"))
  }

  def page2(finish: Person => Html.SHtml)(namesR: Names) = Html.html {
    age |+| button(onclick.read[("age" as String) :: HNil] { ageR =>
      val person = Person(namesR("first"), namesR("last"), ageR("age").toInt)
      finish(person)
    }, text("Finish"))
  }

  def complete(person: Person) = text(person.toString)

  val main: Html[Nothing, HNil] = page1(page2(complete))
}
