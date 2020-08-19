package rec.recordtyped

import rec.{RecordConcat, RecordList}
import shapeless.{HList, HNil}

import scala.annotation.unchecked.uncheckedVariance

trait RecordFList[C[-_, _]] {
  case class RecordFList[-A, Read <: HList](private val list: List[Any]) {
    def :+[NewRead <: HList, Result <: HList](a: C[A @uncheckedVariance, NewRead])(
        implicit
        recordConcat: RecordConcat[NewRead, Read, Result]
    ): RecordFList[A, Result] =
      RecordFList(a :: this.list)

    def ::[NewRead <: HList, Result <: HList](a: C[A @uncheckedVariance, NewRead])(
        implicit
        recordConcat: RecordConcat[NewRead, Read, Result]
    ): RecordFList[A, Result] =
      RecordFList(a :: this.list)

    def :::(a: List[C[A @uncheckedVariance, HNil]]): RecordFList[A @uncheckedVariance, Read] =
      RecordFList(a ::: this.list)

    def toList: List[C[A, _]] = list.asInstanceOf[List[C[A, _]]]

    def toRecordList[NC[_]](rl: RecordList[NC])(
        f: C[A, _] => NC[_]): rl.RecordList[Read] =
      rl.RecordList[Read](list.asInstanceOf[List[C[A, _]]].map(f))
  }

  def Empty[A] = RecordFList[A, HNil](List.empty)
}
