package rec.containerread.arrowpicker

import rec.containerread.Html
import rec.containerread.syntax.DSL
import rec.containerread.syntax.Svg._
import rec.containerread.syntax.SvgAttributes._

object Draw {

  def cross(point: Point, size: Double) = {
    val nx = point.x
    val ny = point.y
    line(
      x1(nx.toString) |+| x2(nx.toString) |+| y1((ny - size / 2).toString) |+| y2(
        (ny + size / 2).toString) |+| stroke("black") |+| strokeWidth("0.01")) |+|
      line(
        y1(ny.toString) |+| y2(ny.toString) |+| x1((nx - size / 2).toString) |+| x2(
          (nx + size / 2).toString) |+| stroke("black") |+| strokeWidth("0.01"))
  }

  def coloredCircle(color: String, radius: Double, position: Point) = {
    circle(
      stroke("black") |+| strokeWidth("0.01") |+|
        cx(position.x.toString) |+| cy(position.y.toString) |+| r(
        radius.toString) |+| fill(color))
  }

  def targetCircle(score: Score) = {
    val radius = Score.toRadius(score).get // error when passing Miss
    val color  = Score.toTargetColor(score)
    coloredCircle(color, radius, Point(0, 0))
  }

  def currentPointView(arrowCoordinate: Point, position: Point) = {
    val radius = arrowCoordinate.size
    val score  = Score.fromRadius(radius)

    val circle = coloredCircle(Score.toTargetColor(score), .2, position)
    val scoreText =
      text(
        x(position.x.toString) |+| y(position.y.toString) |+|
          textAnchor("middle") |+| dominantBaseline("middle") |+|
          fontSize(".2"),
        DSL.text(Score.toShortString(score))
      )
    circle |+| scoreText
  }

  val target = Score.scores.drop(1).map(targetCircle).reduce(_ |+| _)

}
