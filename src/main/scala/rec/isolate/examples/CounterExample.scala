package rec.isolate.examples

import rec.isolate.syntax.DSL._
import rec.isolate.{Html, RecHtmlApp}
import shapeless._
import shapeless.record._

import scala.language.reflectiveCalls

class CounterExample extends RecHtmlApp {
  val fooInput = input(tpe("text") |+| F.value("foo"))
  val barInput = input(tpe("checkbox") |+| F.checked("bar"))

  val main = Html.html {
    fooInput |+| button(onclick.read[("foo" as String) :: HNil] { fooRecord =>
      Html.html {
        barInput |+| button(onclick.read[("bar" as Boolean) :: HNil] { barRecord =>
          text(fooRecord("foo"))
        }, text("Click to break"))
      }
    })
  }
}
