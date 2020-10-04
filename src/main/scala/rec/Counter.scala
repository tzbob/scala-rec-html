package rec

import rec.syntax.DSL._
import shapeless._
import shapeless.record._
import syntax.singleton._

import scala.language.reflectiveCalls

class Counter extends RecHtmlApp {
  type Inc = ("inc" ->> String) :: HNil

  def ex(count: Int, r: Inc): Html[HNil] = {
    val newCount = count + r("inc").toInt
    Html.fix[Inc] { read =>
      val recClick = read(onclick)((r, _) => ex(newCount, r))
      div(
        input(tpe("text"), field("value", "inc".is[String])) ::
          button(recClick, RNil, text("Inc")) ::
          p(text(s"Count $newCount"))
      )
    }
  }

  val html = ex(0, ("inc" ->> "0") :: HNil)
}
