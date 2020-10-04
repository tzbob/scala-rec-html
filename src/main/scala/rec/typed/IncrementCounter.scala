package rec.typed

import rec.typed.syntax.DSL._
import shapeless._
import shapeless.record._
import shapeless.syntax.singleton._

class IncrementCounter extends RecHtmlApp {
  type Inc = ("inc" ->> String) :: HNil
  def ex(count: Int, r: Inc): Html["div", HNil] =
    Html.fix[Inc] { withReader =>
      val newCount = count + r("inc").toInt
      div(
        ANil,
        FNil,
        input(tpe("text"), F.value("inc".is[String]), ENil) ::
          button(withReader(onclick)((r, _) => ex(newCount, r)), FNil, text("Inc")) ::
          p(ANil, FNil, text(s"Count $newCount"))
      )
    }

  val main = ex(0, ("inc" ->> "0") :: HNil)
}
