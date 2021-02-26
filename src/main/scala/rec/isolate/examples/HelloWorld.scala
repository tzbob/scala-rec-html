package rec.isolate.examples

import rec.isolate._
import shapeless._
import shapeless.record._

trait HelloWorld extends RecHtmlApp {
  import rec.isolate.syntax.DSL._

  type Person = ("given" as String) :: ("family" as String) :: HNil

  def hello(person: Person) = p(text(s"Hello ${person("given")} ${person("family")}"))

  val main = Html.html {
    div(
      input(tpe("text") |+| F.value("given")) |+|
        input(tpe("text") |+| F.value("family")) |+|
        button(onclick.read(hello), text("Inc"))
    )
  }
}
