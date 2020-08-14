package rec

import shapeless.{HList, HNil}

trait RecordList[C[_]] {
  case class RecordList[Read <: HList](private val list: List[Any]) {
    def ::[NewRead <: HList, Result <: HList](a: C[NewRead])(
        implicit
        recordConcat: RecordConcat[NewRead, Read, Result]
    ): RecordList[Result] =
      RecordList(a :: this.list)

    def :::(a: List[C[HNil]]): RecordList[Read] = RecordList(a ::: this.list)

    val toList: List[C[_]] = list.asInstanceOf[List[C[_]]]
  }

  val Empty = RecordList[HNil](List.empty)
}
