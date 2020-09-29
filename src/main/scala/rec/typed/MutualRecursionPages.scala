package rec.typed

import rec.typed.syntax.DSL._
import shapeless._

trait MutualRecursionPages extends RecHtmlApp {
  val page1: Html["button", HNil] = button(onclick(_ => page2), FNil, text("Go to page two"))
  val page2: Html["button", HNil] = button(onclick(_ => page1), FNil, text("Go to page one"))

  val main = page1
}
