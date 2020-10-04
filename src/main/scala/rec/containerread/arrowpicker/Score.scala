package rec.containerread.arrowpicker

import scala.collection.immutable.ListMap

sealed abstract class Score

object Score {
  case object Miss  extends Score
  case object One   extends Score
  case object Two   extends Score
  case object Three extends Score
  case object Four  extends Score
  case object Five  extends Score
  case object Six   extends Score
  case object Seven extends Score
  case object Eight extends Score
  case object Nine  extends Score
  case object Ten   extends Score
  case object X     extends Score

  private val scoreValueMap: ListMap[Score, Int] = ListMap(
    (Miss, 0),
    (One, 1),
    (Two, 2),
    (Three, 3),
    (Four, 4),
    (Five, 5),
    (Six, 6),
    (Seven, 7),
    (Eight, 8),
    (Nine, 9),
    (Ten, 10),
    (X, 10)
  )
  private val valueScoreMap: ListMap[Int, Score] =
    (scoreValueMap - X).map(_.swap)

  val scores = scoreValueMap.keys.toVector

  def toShortString(score: Score): String = score match {
    case Miss  => "M"
    case X     => "X"
    case other => scoreValueMap(other).toString
  }

  def toTargetScore(score: Score): Int = scoreValueMap(score)

  def toTargetColor(score: Score) = score match {
    case Miss  => "white"
    case One   => "white"
    case Two   => "white"
    case Three => "#333"
    case Four  => "#333"
    case Five  => "blue"
    case Six   => "blue"
    case Seven => "red"
    case Eight => "red"
    case Nine  => "yellow"
    case Ten   => "yellow"
    case X     => "yellow"
  }

  def toRadius(score: Score): Option[Double] = {
    val radiusScore: Option[Double] = score match {
      case Miss  => None
      case X     => Some(10.5)
      case other => Some(toTargetScore(other).toDouble)
    }
    radiusScore.map(x => (11 - x) / 2)
  }

  def fromRadius(radius: Double): Score = {
    val scoreDouble = -radius * 2 + 11
    if (scoreDouble <= 0) Miss
    else if (scoreDouble >= 10.5) X
    else valueScoreMap(scoreDouble.toInt)
  }

  def fromPoint(point: Point): Score = Score.fromRadius(point.size)

}
