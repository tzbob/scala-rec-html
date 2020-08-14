package rec

import shapeless._
import labelled.{FieldType, field}
import shapeless.ops.hlist.{Intersection, Prepend}
import shapeless.ops.record.Keys

// Complete copy from
//   https://stackoverflow.com/questions/31640565/converting-mapstring-any-to-a-case-class-using-shapeless
trait FromMap[L <: HList] {
  def apply(m: Map[String, Any]): Option[L]
}

trait LowPriorityFromMap {
  implicit def hconsFromMap1[K <: String, V, T <: HList](
      implicit
      witness: Witness.Aux[K],
      fromMapT: Lazy[FromMap[T]]): FromMap[FieldType[K, V] :: T] =
    new FromMap[FieldType[K, V] :: T] {
      def apply(m: Map[String, Any]): Option[FieldType[K, V] :: T] =
        for {
          v <- m.get(witness.value)
          t <- fromMapT.value(m)
        } yield field[K](v.asInstanceOf[V]) :: t
    }
}

object FromMap extends LowPriorityFromMap {
  implicit val hnilFromMap: FromMap[HNil] = new FromMap[HNil] {
    def apply(m: Map[String, Any]): Option[HNil] = Some(HNil)
  }

  implicit def hconsFromMap0[K <: String, V, R <: HList, T <: HList](
      implicit
      witness: Witness.Aux[K],
      gen: LabelledGeneric.Aux[V, R],
      fromMapH: FromMap[R],
      fromMapT: FromMap[T]): FromMap[FieldType[K, V] :: T] =
    new FromMap[FieldType[K, V] :: T] {
      def apply(m: Map[String, Any]): Option[FieldType[K, V] :: T] =
        for {
          v <- m.get(witness.value)
          r <- Typeable[Map[String, Any]].cast(v)
          h <- fromMapH(r)
          t <- fromMapT(m)
        } yield field[K](gen.from(h)) :: t
    }
}
