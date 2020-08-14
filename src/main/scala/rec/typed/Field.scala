package rec.typed

import rec.syntax.DSL
import shapeless.HList

case class Field[Name <: String with Singleton, Read](
    field: rec.Html.Field[Read])

object Field {
  case class Make[Name <: String with Singleton](name: Name) {
    def apply[Read](selector: DSL.FieldSelector[Read]): Field[Name, Read] =
      Field(DSL.field(name, selector))
  }
}
