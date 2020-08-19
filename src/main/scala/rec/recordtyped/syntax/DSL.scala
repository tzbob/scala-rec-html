package rec.recordtyped.syntax

import rec.recordtyped.ChildrenList.ChildrenList
import rec.recordtyped.FieldList.FieldList
import rec.recordtyped.{ChildrenList, FieldList}
import rec.syntax.DSL.FieldSelector
import shapeless._
import shapeless.labelled.FieldType

object DSL extends Tags {

  type ->>[K, V] = FieldType[K, V]

  def FNil[F]: FieldList[F, HNil]    = FieldList.Empty[F]
  def CNil[C]: ChildrenList[C, HNil] = ChildrenList.Empty[C]

  implicit class FieldSelectorMaker[K <: Singleton with String](str: K)(
      implicit witness: Witness.Aux[K]) {
    def is[V]: FieldSelector[FieldType[K, V] :: HNil] =
      FieldSelector[FieldType[K, V] :: HNil](str)
  }

  implicit def CL[C, Read <: HList](
      f: C => rec.Html[Read]): ChildrenList[C, Read] =
    ChildrenList.RecordFList[C, Read](List(f))

  implicit def FL[F, Read <: HList](
      f: F => rec.Html.Field[Read]): FieldList[F, Read] =
    FieldList.RecordFList[F, Read](List(f))

  implicit def liftToFL[F, Read <: HList](
      f: rec.Html.Field[Read]): F => rec.Html.Field[Read] = _ => f

  implicit def liftToCLFun[C, Read <: HList](
      f: rec.Html[Read]): C => rec.Html[Read] = _ => f
}
