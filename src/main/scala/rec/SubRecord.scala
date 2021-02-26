package rec

import shapeless.HList
import shapeless.ops.hlist.Intersection

import scala.annotation.implicitNotFound

@implicitNotFound("${R2} is not a substitution for ${R1}, missing fields!")
trait SubRecord[R1 <: HList, R2 <: HList]

object SubRecord {
  implicit def subrecord[R1 <: HList, R2 <: HList](
      implicit ev: Intersection.Aux[R1, R2, R1]) = new SubRecord[R1, R2] {}
}
