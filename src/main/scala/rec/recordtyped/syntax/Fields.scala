package rec.recordtyped.syntax

import rec.Html.Field
import rec.syntax.DSL.FieldSelector

trait Fields {

  case class FieldMaker(name: String) {
    def apply[R](field: FieldSelector[R]): Field[R] =
      Field[R](name, field.fieldName)
  }

  trait ElementFields {
    val innerText = FieldMaker("innerText")
  }

  trait InputFields {
    val value   = FieldMaker("value")
    val checked = FieldMaker("checked")
  }
}

object Fields extends Fields
