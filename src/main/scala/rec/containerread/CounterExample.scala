package rec.containerread

import rec.containerread.syntax.DSL._
import shapeless._
import shapeless.record._

import scala.language.reflectiveCalls

class CounterExample {
//  type Foo = ("foo" ->> String) :: HNil
//  type Bar = ("bar" ->> Boolean) :: HNil
//
//  val foo = input(tpe("text"), field("value", "foo".is[String]))
//  val bar = input(tpe("checkbox"), field("checked", "bar".is[Boolean]))
//
//  val html = Html.isolate {
//    def switchToBar(foo: Foo): Html[Nothing, HNil] = Html.isolate {
//      val btn = button(onclick.read[Foo] { r =>
//        println(r)
//        switchToBar(r)
//      }, text("Crash me:" + foo("foo")))
//      div(bar |+| btn)
//    }
//
//    val btn = button(onclick.read[Foo] { r =>
//      switchToBar(r)
//    }, text("To bar"))
//    div(foo |+| btn)
//  }
}
