package rec

import shapeless.ops.hlist.{Align, Intersection, Prepend}
import shapeless.ops.record.Keys
import shapeless.{HList, HNil}

import scala.annotation.implicitNotFound

@implicitNotFound(
  "Cannot find a way to concatenate records ${R1} and ${R2}, check for duplicate fields."
)
trait RecordConcat[R1 <: HList, R2 <: HList, Concat <: HList] {
  def apply(r1: R1, r2: R2): Concat
}

object RecordConcat {
  implicit class RecordConcatOps[R1 <: HList](hlist: R1) {
    def &&[R2 <: HList, Result <: HList](other: R2)(
        implicit concat: RecordConcat[R1, R2, Result]): Result =
      concat(hlist, other)
  }

  implicit def hconsRecordConcat[R1 <: HList,
                                 R2 <: HList,
                                 K1 <: HList,
                                 K2 <: HList,
                                 SubResult <: HList,
                                 Result <: HList](
      implicit k1: Keys.Aux[R1, K1],
      k2: Keys.Aux[R2, K2],
      noDupeKeys: Intersection.Aux[K1, K2, HNil],
      prepended: Prepend.Aux[R1, R2, SubResult],
      align: Align[SubResult, Result]): RecordConcat[R1, R2, Result] =
    new RecordConcat[R1, R2, Result] {
      override def apply(r1: R1, r2: R2): Result = align(prepended(r1, r2))
    }

  implicit val hconsHNilHNilRecordConcat: RecordConcat[HNil, HNil, HNil] =
    new RecordConcat[HNil, HNil, HNil] {
      override def apply(r1: HNil, r2: HNil): HNil = HNil
    }
  implicit def hconsHNilRecordConcatR[R <: HList]: RecordConcat[HNil, R, R] =
    new RecordConcat[HNil, R, R] {
      override def apply(r1: HNil, r2: R): R = r2
    }
  implicit def hconsHNilRecordConcatL[R <: HList]: RecordConcat[R, HNil, R] =
    new RecordConcat[R, HNil, R] {
      override def apply(r1: R, r2: HNil): R = r1
    }
}
