package rec.isolate

import rec.{FromMap, RecordConcat, SubRecord}
import shapeless._

sealed trait Html[+Use, Bind]

object Html {
  type SHtml = Html[Nothing, HNil]

  implicit class CombineHtml[Use, BindL <: HList](html: Html[Use, BindL]) {
    def |+|[BindR <: HList, Bind <: HList](other: Html[Use, BindR])(
        implicit recordConcat: RecordConcat[BindL, BindR, Bind]): Html[Use, Bind] =
      Combine(html, other)
  }

  val empty: Html[Nothing, HNil] = Nil
  case object Nil              extends Html[Nothing, HNil]
  case class Text(str: String) extends Html[Nothing, HNil]
  case class Element[Use, BindF, BindC, Bind](tag: String,
                                              attribute: Attribute[Use, BindF],
                                              child: Html[Use, BindC])
      extends Html[Use, Bind]

  case class RequestAnimationFrame[Use, Bind](after: () => Html[Use, Bind]) extends Html[Use, Bind]

  def onAnimationFrame[Use, Bind](after: () => Html[Use, Bind]) =
    RequestAnimationFrame(after)

  case class Combine[Use, BindL <: HList, BindR <: HList, Bind <: HList](
      left: Html[Use, BindL],
      right: Html[Use, BindR])(implicit recordConcat: RecordConcat[BindL, BindR, Bind])
      extends Html[Use, Bind]

  case class Contained[Use <: HList, Bind <: HList](html: Html[Use, Bind])(
      implicit hasAll: SubRecord[Use, Bind])
      extends Html[Nothing, HNil]

  def html[Use <: HList, Bind <: HList](html: Html[Use, Bind])(
      implicit hasAll: SubRecord[Use, Bind],
      fromMap: FromMap[Use]
  ): Html[Nothing, HNil] = Contained(html)

  def fromSeq[Use](seq: Seq[Html[Use, HNil]]): Html[Use, HNil] = {
    seq.foldLeft(Html.empty: Html[Use, HNil]) { (left, right) =>
      Combine(left, right)
    }
  }
}
