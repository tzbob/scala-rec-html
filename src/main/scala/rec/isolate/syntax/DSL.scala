package rec.isolate.syntax

import rec.isolate._
import shapeless._
import shapeless.labelled.FieldType

import scala.language.implicitConversions

trait DSL extends Tags with Attributes.Attrs with Events {
  type as[K, V]  = FieldType[K, V]
  type ->>[K, V] = FieldType[K, V]

  val F = Fields

  val Svg     = rec.isolate.syntax.Svg
  val SvgAttr = rec.isolate.syntax.SvgAttributes

  val text: String => Html[Nothing, HNil] = str => Html.Text(str)
  case class FieldSelector[R](fieldName: String)
  def field[R](attribute: String, field: FieldSelector[R]): Attribute[Nothing, R] =
    Attribute.Field[R](attribute, field.fieldName)
}

object DSL extends DSL {
  implicit class FieldSelectorMaker[K <: Singleton with String](str: K)(
    implicit witness: Witness.Aux[K]) {
    def is[V]: FieldSelector[FieldType[K, V] :: HNil] =
      FieldSelector[FieldType[K, V] :: HNil](str)
  }
}
