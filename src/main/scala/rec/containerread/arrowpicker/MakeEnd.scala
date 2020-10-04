package rec.containerread.arrowpicker

import rec.containerread.Html
import shapeless.HNil

object MakeEnd {
  import rec.containerread.syntax.DSL._

  def end(state: State): Html[Nothing, HNil] = Html.isolate {
    val targetPicker = ArrowPicker.targetPicker(end, state)

    val underDiv = state match {
      case State.Idle(viewBox) => text(s"viewbox $viewBox")
      case State.Picking(viewBox, point) =>
        text(s"viewbox $viewBox point $point")
      case State.Picked(_, score) =>
        text(s"GOT $score")
    }

    article(targetPicker) |+| footer(button(text("Finish")) |+| underDiv)
  }

}
