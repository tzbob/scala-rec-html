package rec.isolate.syntax

import rec.isolate.Attribute
import rec.isolate.syntax.DSL.FieldSelector
import shapeless._
import shapeless.labelled.FieldType

object Fields {

  case class FieldMaker[V](attribute: String) {
    def apply[K <: Singleton with String](
        fieldName: K): Attribute[Nothing, FieldType[K, V] :: HNil] =
      Attribute.Field(attribute, fieldName)
  }

  val value   = FieldMaker[String]("value")
  val checked = FieldMaker[Boolean]("checked")
}
