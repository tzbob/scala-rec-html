package rec.typed.syntax

import rec.syntax.DSL.FieldSelector
import rec.typed.Field

object Fields extends Fields
trait Fields {
  type ElementFields = "innerText"
  val innerText = Field.Make("innerText")

  type InputFields = "value" with "checked" with ElementFields
  val value   = Field.Make("value")
  val checked = Field.Make("checked")
}
