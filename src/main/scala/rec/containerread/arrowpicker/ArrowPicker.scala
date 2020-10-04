package rec.containerread.arrowpicker

import org.scalajs.dom
import org.scalajs.dom.raw.MouseEvent
import rec.containerread.syntax.Attributes
import rec.containerread.syntax.DSL._
import rec.containerread.{Html, RecHtmlApp}
import shapeless.HNil

object ArrowPicker {

  def targetPicker(picker: State => Html[Nothing, HNil],
                   state: State): Html[Nothing, HNil] = Html.isolate {
    import Svg._

    def arrowCoordinate(point: Point) = point - Point(0, 1)

    val arrow = state match {
      case State.Picking(_, point) =>
        Draw.cross(arrowCoordinate(point), 0.2) |+|
          Draw.currentPointView(arrowCoordinate(point), point - Point(.5, 1))
      case _ => Html.empty
    }

    def moveListener(viewBox: ViewBox, point: Point) =
      onpointermove.listen { e =>
        e.preventDefault()
        val newPoint = Point.fromSvgClick(e.asInstanceOf[MouseEvent])
        val shift    = (newPoint - point) / 2
        val newState =
          State.Picking(viewBox.translate(shift), newPoint)
        picker(newState)
      }

    def downListener(viewBox: ViewBox) =
      onpointerdown.listen { e =>
        e.preventDefault()
        val point   = Point.fromSvgClick(e.asInstanceOf[MouseEvent])
        val newView = viewBox.zoomOnPoint(point, 2)
        val nState  = State.Picking(newView, point)
        picker(nState)
      }

    def upListener() = {
      val onStopMove = { (e: dom.Event) =>
        e.preventDefault()
        val point = Point.fromSvgClick(e.asInstanceOf[MouseEvent])
        picker(
          State.Picked(ViewBox.default,
                       Score.fromPoint(arrowCoordinate(point))))
      }
      onpointerup.listen(onStopMove) |+| onpointercancel.listen(onStopMove)
    }

    val listeners = state match {
      case State.Picking(viewBox, point) =>
        upListener() |+| moveListener(viewBox, point)
      case _ => downListener(ViewBox.default)
    }

    svg(
      Attributes
        .attr("style")("touch-action: none;") |+| listeners |+| state.viewBox.attr,
      Draw.target |+| Draw.cross(Point(0, 0), 0.1) |+| arrow)
  }
}
