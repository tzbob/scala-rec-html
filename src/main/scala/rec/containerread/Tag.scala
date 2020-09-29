package rec.containerread

import rec.RecordConcat
import shapeless.{HList, HNil}

case class Tag(name: String) {
  // All arguments

  def apply[Use, BindF <: HList, BindC <: HList, Bind <: HList](
      attributes: Attribute[Use],
      fields: Field[BindF],
      children: Html[Use, BindC])(
      implicit recordConcatB: RecordConcat[BindF, BindC, Bind]
  ): Html[Use, Bind] = Html.Element(name, attributes, fields, children)

  // Two arguments
  def apply[Use, BindF <: HList, BindC <: HList, Bind <: HList](
      fields: Field[BindF],
      children: Html[Use, BindC])(
      implicit recordConcatB: RecordConcat[BindF, BindC, Bind]
  ): Html[Use, Bind] = Html.Element(name, Attribute.Nil, fields, children)

  def apply[Use, Bind <: HList](attributes: Attribute[Use],
                                children: Html[Use, Bind]): Html[Use, Bind] =
    Html.Element(name, attributes, Field.Nil, children)

  def apply[Use, Bind <: HList](attributes: Attribute[Use],
                                fields: Field[Bind]): Html[Use, Bind] =
    Html.Element(name, attributes, fields, Html.Nil)

  // 1 Argument
  def apply[Use, Bind <: HList](children: Html[Use, Bind]): Html[Use, Bind] =
    Html.Element(name, Attribute.Nil, Field.Nil, children)

  def apply[Use, Bind <: HList](fields: Field[Bind]): Html[Nothing, Bind] =
    Html.Element(name, Attribute.Nil, fields, Html.Nil)

  def apply[Use](attributes: Attribute[Use]): Html[Use, HNil] =
    Html.Element(name, attributes, Field.Nil, Html.Nil)
}
