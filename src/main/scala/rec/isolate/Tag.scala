package rec.isolate

import rec.RecordConcat
import shapeless.{HList, HNil}

case class Tag(name: String) {
  // All arguments

  def apply[Use, BindF <: HList, BindC <: HList, Bind <: HList](attributes: Attribute[Use, BindF],
                                                                children: Html[Use, BindC])(
      implicit recordConcatB: RecordConcat[BindF, BindC, Bind]
  ): Html[Use, Bind] = Html.Element(name, attributes, children)

  def apply[Use, Bind <: HList](attributes: Attribute[Use, Bind]): Html[Use, Bind] =
    Html.Element(name, attributes, Html.Nil)

  def apply[Use, Bind <: HList](children: Html[Use, Bind]): Html[Use, Bind] =
    Html.Element(name, Attribute.Nil, children)

  def apply(): Html[Nothing, HNil] =
    Html.Element(name, Attribute.Nil, Html.Nil)
}
