package rec

import rec.Html._
import rec.syntax.DSL._

import scala.language.reflectiveCalls
import scala.scalajs.js

trait Person extends js.Object {
  val last: String = js.native; val first: String = js.native
}

class ViewPerson extends RecHtmlApp {
  val lastname =
    input(tpe("text"), field[{ val last: String }]("value", "last"), NNil)
  val firstname =
    input(tpe("text"), field[{ val first: String }]("value", "first"), NNil)

  val personInput = div(Nil, RNil, lastname :: firstname)

  def showPerson(person: Person): Html[_] = {
    val back = button(List(onclick(_ => inputPerson)), RNil, Text("Back"))
    div(Nil, RNil, text(s"Person: ${person.last} ${person.first}") :: back)
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
