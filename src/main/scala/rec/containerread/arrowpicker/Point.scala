package rec.containerread.arrowpicker

import org.scalajs.dom.MouseEvent
import org.scalajs.dom.raw.SVGSVGElement

case class Point(x: Double, y: Double) {
  def *(scale: Double) = Point(x * scale, y * scale)
  def /(scale: Double) = this * (1 / scale)
  def +(point: Point)  = Point(x + point.x, y + point.y)
  def -(point: Point)  = this + (point * -1)

  val size = Math.sqrt(x * x + y * y)
}

object Point {
  def fromSvgClick(evt: MouseEvent) = {
    val svgEl = evt.currentTarget.asInstanceOf[SVGSVGElement]
    val pt    = svgEl.createSVGPoint()
    pt.x = evt.clientX
    pt.y = evt.clientY
    val svgPoint =
      pt.matrixTransform(svgEl.getScreenCTM().inverse())
    Point(svgPoint.x, svgPoint.y)
  }
}
