package rec.recordtyped

import shapeless.HList

object FieldList
    extends RecordFList[({ type F[-A, R] = A => rec.Html.Field[R] })#F] {
  type FieldList[A, R <: HList] = this.RecordFList[A, R]

}
