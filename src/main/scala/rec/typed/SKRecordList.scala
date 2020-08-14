package rec.typed

import rec.{RecordConcat, RecordList}
import shapeless.{HList, HNil}

trait SKRecordList[C[_ <: String with Singleton, _], Constraint[A, B]] {
  case class SKRecordList[Set, Read <: HList](private val list: List[Any]) {
    def ::[A <: String with Singleton, NewRead <: HList, Result <: HList](
        a: C[A, NewRead])(
        implicit ev: A Constraint Set,
        recordConcat: RecordConcat[NewRead, Read, Result]
    ): SKRecordList[Set with A, Result] =
      SKRecordList(a :: this.list)

    def toRecordList[NC[_]](rl: RecordList[NC])(
        f: C[_, _] => NC[_]): rl.RecordList[Read] =
      rl.RecordList[Read](list.asInstanceOf[List[C[_, _]]].map(f))
  }

  implicit def apply[A <: String with Singleton, Read <: HList](
      c: C[A, Read]): SKRecordList[A, Read] = SKRecordList[A, Read](List(c))

  val empty = SKRecordList["", HNil](List.empty)
}
