package rec.containerread

import rec.RecordConcat
import shapeless.HList

case class Tag(name: String) {
  def apply[Use, BindF <: HList, BindC <: HList, Bind <: HList](
      attributes: Attribute[Use],
      fields: Field[BindF],
      children: Html[Use, BindC])(
      implicit recordConcatB: RecordConcat[BindF, BindC, Bind]
  ): Html[Use, Bind] = Html.Element(name, attributes, fields, children)
}
