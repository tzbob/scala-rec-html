package rec.recordtyped

import rec.RecHtmlApp
import rec.recordtyped.Html.Html
import rec.recordtyped.syntax.DSL._
import rec.syntax.EventBindMaker
import shapeless._
import shapeless.record._

class ViewPerson extends RecHtmlApp {
  type Person = ("last" ->> String) :: ("first" ->> String) :: HNil

  val lastname = // <input type="text" value=#last/>
    input(Nil :+ (_.tpe("text")), FNil :+ (_.value("last".is[String])), CNil)
  val firstname: Html[("first" ->> String) :: HNil] = // <input type="text" value=#first/>
    input(Nil :+ (_.tpe("text")), FNil :+ (_.value("first".is[String])), CNil)

  // <div>lastname firstname</div>
  val personInput = div(Nil, FNil, lastname :: CL(firstname))

  def showPerson(person: Person): Html[HNil] = {
    // <button onclick(_ => inputPerson)>Back</button>
    val back =
      button(Nil :+ (_.onclick(_ => inputPerson)), FNil, CNil :+ text("Back"))
    // <div>(s"Person: ${person.last} ${person.first}") back</div>
    div(Nil, FNil, text(s"Person: ${person.apply("last")} ${person("first")}") :: back :: CNil)
  }

  val inputPerson = Html.fix[Person] { bindWithReader =>
    val clickBind = bindWithReader(
      /*can be inferred with record selectors*/
      (_: { val onclick: EventBindMaker }).onclick) { (person, _) =>
      showPerson(person)
    }

    // <button clickBind>Submit</button>
    val btn = button(Nil :+ clickBind, FNil, CNil :+ text("Submit"))
    // <div>personInput btn</div>
    div(Nil, FNil, personInput :: btn :: CNil)
  }

  val html = inputPerson
}
