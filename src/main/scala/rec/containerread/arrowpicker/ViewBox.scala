package rec.containerread.arrowpicker

import rec.containerread.syntax.SvgAttributes

case class ViewBox(point: Point, width: Double, height: Double) {
  val attr = SvgAttributes.viewBox(s"${point.x} ${point.y} $width $height")

  def translate(target: Point) =
    copy(point = Point(point.x + target.x, point.y + target.y))

  def *(scale: Double) = {
    def shift(wh: Double) = (wh * scale - wh) / 2

    val xShift = shift(width)
    val yShift = shift(height)

    ViewBox(point - Point(xShift, yShift), width * scale, height * scale)
  }

  def zoomOnPoint(point: Point, zoom: Double) = {
    val scale        = 1 / zoom
    val shiftAndZoom = translate(point) * scale
    val recenter     = shiftAndZoom.translate(point * -scale)
    recenter
  }
}

object ViewBox {
  val default = ViewBox(Point(-6, -6), 12, 12)
}
