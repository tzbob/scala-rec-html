package rec.containerread

import rec.RecordConcat
import shapeless.{HList, HNil}

sealed trait Field[Bind]

object Field {
  implicit class CombineField[BindL <: HList](left: Field[BindL]) {
    def |+|[BindR <: HList, Bind <: HList](right: Field[BindR])(
        implicit recordConcat: RecordConcat[BindL, BindR, Bind]): Field[Bind] =
      Combine(left, right)
  }

  case class FieldImpl[Bind](key: String, fieldName: String) extends Field[Bind]
  case object Nil                                            extends Field[HNil]

  case class Combine[BindL <: HList, BindR <: HList, Bind <: HList](
      left: Field[BindL],
      right: Field[BindR])(
      implicit recordConcat: RecordConcat[BindL, BindR, Bind])
      extends Field[Bind]

  def list(field: Field[_]): List[Field.FieldImpl[_]] = field match {
    case Combine(left, right) => list(left) ::: list(right)
    case Nil                  => List.empty
    case f @ FieldImpl(_, _)  => List(f)
  }
}
