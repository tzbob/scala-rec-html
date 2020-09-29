package rec.typed

import rec.typed.syntax.DSL._
import shapeless._
import shapeless.record._
import shapeless.syntax.singleton._

trait IncrementByOne extends RecHtmlApp {
  def ex(count: Int): Html["button", HNil] =
    button(onclick(_ => ex(count + 1)), FNil, text(s"Count: $count"))
  val main = ex(0)
}
