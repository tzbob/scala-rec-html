package rec.containerread

import rec.{FromMap, RecordConcat}
import shapeless._
import shapeless.ops.hlist.Intersection

sealed trait Html[+Use, Bind]

object Html {
  type SubRecord[R1 <: HList, R2 <: HList] = Intersection.Aux[R1, R2, R1]

  implicit class CombineHtml[Use, BindL <: HList](html: Html[Use, BindL]) {
    def |+|[BindR <: HList, Bind <: HList](other: Html[Use, BindR])(
        implicit recordConcat: RecordConcat[BindL, BindR, Bind])
      : Html[Use, Bind] = Combine(html, other)
  }

  case object Nil              extends Html[Nothing, HNil]
  case class Text(str: String) extends Html[Nothing, HNil]
  case class Element[Use, BindF, BindC, Bind](tag: String,
                                              attribute: Attribute[Use],
                                              field: Field[BindF],
                                              child: Html[Use, BindC])
      extends Html[Use, Bind]

  case class Combine[Use, BindL <: HList, BindR <: HList, Bind <: HList](
      left: Html[Use, BindL],
      right: Html[Use, BindR])(
      implicit recordConcat: RecordConcat[BindL, BindR, Bind])
      extends Html[Use, Bind]

  case class Contained[Use <: HList, Bind <: HList](html: Html[Use, Bind])(
      implicit hasAll: SubRecord[Use, Bind])
      extends Html[Nothing, HNil]

  def isolate[Use <: HList, Bind <: HList](html: Html[Use, Bind])(
      implicit hasAll: SubRecord[Use, Bind],
      fromMap: FromMap[Use]
  ): Html[Nothing, HNil] = Contained(html)

}
