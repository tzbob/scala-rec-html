package rec

import scala.annotation.unchecked.uncheckedVariance

trait NodeList[-Read] {
  def ::[RL](a: Html[RL]): NodeList.NCons[RL, Read] =
    NodeList.NCons[RL, Read](a, this)
  def :::[RL](a: List[Html[RL]]): NodeList.ListCons[RL, Read] =
    NodeList.ListCons[RL, Read](a, this)
}

object NodeList {
  case object Empty extends NodeList[{}]
  case class NCons[-RL, -L](a: Html[RL], list: NodeList[L])
      extends NodeList[RL with L] {
    val widen: NodeList[RL with L] = this
  }

  case class ListCons[-RL, -L](a: List[Html[RL @uncheckedVariance]],
                               list: NodeList[L])
      extends NodeList[({ def list: List[RL @uncheckedVariance] }) with L] {
    val widen: NodeList[({ def list: List[RL @uncheckedVariance] }) with L] =
      this
  }

  def toList(readList: NodeList[_]): List[Html[_]] = readList match {
    case NCons(a, list)    => a :: toList(list)
    case Empty             => Nil
    case ListCons(a, list) => a ::: toList(list)
  }
}
