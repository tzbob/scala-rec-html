package rec.recordtyped

import shapeless.HList

object ChildrenList
    extends RecordFList[({ type F[-A, R] = A => rec.Html[R] })#F] {
  type ChildrenList[A, R <: HList] = this.RecordFList[A, R]
}
