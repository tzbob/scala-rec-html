package rec

import shapeless.ops.hlist.{Align, Intersection, Prepend}
import shapeless.ops.record.Keys
import shapeless.{HList, HNil}

import scala.annotation.implicitNotFound

@implicitNotFound(
  "Cannot find a way to concatenate records ${R1} and ${R2}, check for duplicate fields."
)
case class RecordConcat[R1 <: HList, R2 <: HList, Concat <: HList]()

object RecordConcat {
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
    RecordConcat()

  implicit val hconsHNilHNilRecordConcat: RecordConcat[HNil, HNil, HNil] =
    RecordConcat()
  implicit def hconsHNilRecordConcatR[R <: HList]: RecordConcat[HNil, R, R] =
    RecordConcat()
  implicit def hconsHNilRecordConcatL[R <: HList]: RecordConcat[R, HNil, R] =
    RecordConcat()
}
