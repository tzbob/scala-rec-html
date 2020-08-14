package rec.typed.syntax

import rec.syntax.DSL.FieldSelector
import rec.typed.AttrSet.AttrSet
import rec.typed.{AttrSet, ElementList, FieldList}
import rec.typed.FieldList.FieldList
import rec.typed.ElementList.ElementList
import shapeless._
import shapeless.{HNil, Witness}
import shapeless.labelled.FieldType

object DSL
    extends Tags
    with Attributes
    with ElementList.Syntax
    with FieldList.Syntax
    with AttrSet.Syntax {
  type ->>[K, V] = FieldType[K, V]

  val ANil: AttrSet[""]           = AttrSet.empty
  val FNil: FieldList["", HNil]   = FieldList.empty
  val ENil: ElementList["", HNil] = ElementList.empty

  implicit class FieldSelectorMaker[K <: Singleton with String](str: K)(
      implicit witness: Witness.Aux[K]) {
    def is[V]: FieldSelector[FieldType[K, V] :: HNil] =
      FieldSelector[FieldType[K, V] :: HNil](str)
  }
}
