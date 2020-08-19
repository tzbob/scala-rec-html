package rec

import shapeless.HList

object NodeList extends RecordList[Html] {
  type NodeList[R <: HList] = this.RecordList[R]
}
