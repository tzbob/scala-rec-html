package rec.typed

import rec.ReadList.ReadList
import rec.typed.NotSubSetOf.⊄
import shapeless.HList

object FieldList extends SKRecordList[Field, ⊄] {
  type FieldList[Set, R <: HList] = this.SKRecordList[Set, R]

  trait Syntax {
    implicit def applyField[A <: String with Singleton, Read <: HList](
        c: Field[A, Read]): SKRecordList[A, Read] =
      SKRecordList[A, Read](List(c))
  }
}
