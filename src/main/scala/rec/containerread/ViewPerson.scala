package rec.containerread

import rec.containerread.syntax.DSL._
import shapeless._
import shapeless.record._

import scala.language.reflectiveCalls

object ViewPerson extends RecHtmlApp {
  type Person = ("last" ->> String) :: ("first" ->> String) :: HNil

  val lastname = // <input type="text" value=#last/>
    input(tpe("text"), field("value", "last".is[String]), Html.Nil)
  val firstname = // <input type="text" value=#first/>
    input(tpe("text"), field("value", "first".is[String]), Html.Nil)

  // <div>lastname firstname</div>
  val personInput = div(Attribute.Nil, Field.Nil, lastname |+| firstname)

  def showPerson(person: Person): Html[Nothing, HNil] = Html.isolate {
    // <button onclick(_ => inputPerson)>Back</button>
    val back =
      button(onclick.as(inputPerson), Field.Nil, text("Back"))
    // <div>(s"Person: ${person.last} ${person.first}") back</div>
    div(Attribute.Nil,
        Field.Nil,
        text(s"Person: ${person.apply("last")} ${person("first")}") |+| back)
  }

  val inputPerson = Html.isolate {
    val clickBind = onclick.read(showPerson)

    // <button clickBind>Submit</button>
    val btn = button(clickBind, Field.Nil, text("Submit"))
    // <div>personInput btn</div>
    div(Attribute.Nil, Field.Nil, personInput |+| btn)
  }

  val html = inputPerson
}
