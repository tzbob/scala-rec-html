package rec

import rec.syntax.DSL._
import shapeless._
import shapeless.record._

import scala.language.reflectiveCalls

trait CounterExample extends RecHtmlApp {
  type Foo = ("foo" ->> String) :: HNil
  type Bar = ("bar" ->> Boolean) :: HNil

  val foo = input(tpe("text"), field("value", "foo".is[String]))
  val bar = input(tpe("checkbox"), field("checked", "bar".is[Boolean]))

  val html = Html.fix[Foo] { readFoo =>
    def switchToBar(foo: Foo): Html[HNil] = Html.fix[Bar] { readBar =>
      val btn: Html[HNil] = button(readFoo(onclick) { (r, _) =>
        println(r)
        switchToBar(r)
      }, RNil, text("Crash me:" + foo("foo")))
      div(bar :: btn)
    }

    val btn: Html[HNil] = button(readFoo(onclick) { (r, _) =>
      switchToBar(r)
    }, RNil, text("To bar"))
    div(foo :: btn)
  }
}
