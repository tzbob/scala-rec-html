package rec.typed

import rec.typed.syntax.DSL._
import shapeless._
import shapeless.record._

import scala.language.reflectiveCalls

object ViewPerson extends RecHtmlApp {
  type Person = ("last" ->> String) :: ("first" ->> String) :: HNil

  val lastname = // <input type="text" value=#last/>
    input(`type`("text"), F.value("last".is[String]), ENil)
  val firstname = // <input type="text" value=#first/>
    input(`type`("text"), F.value("first".is[String]), ENil)

  // <div>lastname firstname</div>
  val personInput = div(ANil, FNil, lastname :: firstname)

  def showPerson(person: Person): Html["div", HNil] = {
    // <button onclick(_ => inputPerson)>Back</button>
    val back = button(onclick(_ => inputPerson), FNil, text("Back"))
    // <div>(s"Person: ${person.last} ${person.first}") back</div>
    div(ANil,
        FNil,
        text(s"Person: ${person.apply("last")} ${person("first")}") :: back)
  }

  val inputPerson = Html.fix["click", "div", Person] { bindWithReader =>
    val clickBind = bindWithReader(onclick) { (person, _) =>
      println("Click triggered")
      showPerson(person)
    }

    // <button clickBind>Submit</button>
    val btn = button(clickBind, FNil, text("Submit"))
    // <div>personInput btn</div>
    div(ANil, FNil, personInput :: btn)
  }

  val typedHtml = inputPerson
}
