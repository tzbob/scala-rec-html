package rec.containerread.syntax

import rec.containerread._
import shapeless._
import shapeless.labelled.FieldType

import scala.language.implicitConversions

object DSL extends Tags with Attributes.Attrs with Events {
  type ->>[K, V] = FieldType[K, V]

  case class FieldSelector[R](fieldName: String)
  implicit class FieldSelectorMaker[K <: Singleton with String](str: K)(
      implicit witness: Witness.Aux[K]) {
    def is[V]: FieldSelector[FieldType[K, V] :: HNil] =
      FieldSelector[FieldType[K, V] :: HNil](str)
  }

  val text: String => Html[Nothing, HNil] = str => Html.Text(str)
  def field[R](attribute: String, field: FieldSelector[R]): Field[R] =
    Field.FieldImpl[R](attribute, field.fieldName)
}
