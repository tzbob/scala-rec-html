package rec.isolate.examples

import rec.isolate._
import rec.isolate.syntax.DSL._
import shapeless._
import shapeless.record._

trait FieldCompare extends RecHtmlApp {
  type F1   = ("f1" as String) :: HNil
  type F2   = ("f2" as String) :: HNil
  type F1F2 = ("f1" as String) :: ("f2" as String) :: HNil

  val main = Html.html {
    div(
      input(tpe("text") |+| field("value", "f1".is[String])) |+|
        input(tpe("text") |+| field("value", "f2".is[String])) |+|
        button(onclick.read[F1F2] { f1f2 =>
          // Action
          if (f1f2("f1") == f1f2("f2")) ??? // Act!
          else main
        }, text("Act"))
    )
  }
}
