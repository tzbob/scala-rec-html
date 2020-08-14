package rec

import rec.syntax.DSL._
import shapeless._
import shapeless.record._

import scala.language.reflectiveCalls

class ViewPerson extends RecHtmlApp {
  type Person = ("last" ->> String) :: ("first" ->> String) :: HNil

  val lastname = // <input type="text" value=#last/>
    input(tpe("text"), field("value", "last".is[String]), NNil)
  val firstname = // <input type="text" value=#first/>
    input(tpe("text"), field("value", "first".is[String]), NNil)

  // <div>lastname firstname</div>
  val personInput = div(Nil, RNil, lastname :: firstname)

  def showPerson(person: Person): Html[_] = {
    // <button onclick(_ => inputPerson)>Back</button>
    val back = button(List(onclick(_ => inputPerson)), RNil, text("Back"))
    // <div>(s"Person: ${person.last} ${person.first}") back</div>
    div(Nil,
        RNil,
        text(s"Person: ${person.apply("last")} ${person("first")}") :: back)
  }

  val inputPerson = Html.fix[Person] { bindWithReader =>
    val clickBind = bindWithReader(onclick) { (person, _) =>
      showPerson(person)
    }

    // <button clickBind>Submit</button>
    val btn = button(clickBind, RNil, text("Submit"))
    // <div>personInput btn</div>
    div(Nil, RNil, personInput :: btn)
  }

  val html = inputPerson
}
