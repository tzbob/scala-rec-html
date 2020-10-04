package rec.containerread.arrowpicker

sealed trait State { val viewBox: ViewBox }

object State {
  case class Idle(viewBox: ViewBox)                    extends State
  case class Picking(viewBox: ViewBox, point: Point) extends State
  case class Picked(viewBox: ViewBox, score: Score)    extends State

  case class StateException(state: State)
      extends RuntimeException(s"Did not expect state $state")
}
