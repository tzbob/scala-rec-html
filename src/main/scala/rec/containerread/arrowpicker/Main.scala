package rec.containerread.arrowpicker

import rec.containerread.RecHtmlApp

object Main extends RecHtmlApp {
  val main = MakeEnd.end(State.Idle(ViewBox.default))
}
