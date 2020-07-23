package rec

import rec.Html.Field

import scala.annotation.unchecked.uncheckedVariance

trait ReadList[-Read] {
  def ::[RL](a: Field[RL]): ReadList.Cons[RL, Read] =
    ReadList.Cons[RL, Read](a, this)
}

object ReadList {
  case object Empty extends ReadList[{}]
  case class Cons[-RL, -L](a: Field[RL @uncheckedVariance], list: ReadList[L])
      extends ReadList[RL with L]

  def toList(readList: ReadList[_]): List[Field[_]] = readList match {
    case Cons(a, list) => a :: toList(list)
    case Empty         => Nil
  }
}
