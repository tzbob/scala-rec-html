package rec

import rec.NodeList.NodeList
import rec.ReadList.ReadList
import shapeless.{HList, HNil}

case class Tag(name: String) {
  // All arguments

  def apply[BindF <: HList, BindC <: HList, Bind <: HList](
      attributes: Seq[Attr],
      reads: ReadList[BindF],
      children: NodeList[BindC])(
      implicit recordConcat: RecordConcat[BindF, BindC, Bind]): Html[Bind] =
    Html.Element(name, attributes, reads, children)

  // Two arguments

  def apply[BindF <: HList, BindC <: HList, Bind <: HList](
      reads: ReadList[BindF],
      children: NodeList[BindC])(
      implicit recordConcat: RecordConcat[BindF, BindC, Bind]): Html[Bind] =
    Html.Element(name, Nil, reads, children)

//  def apply[Bind <: HList](attributes: Seq[Attr],
//                           children: NodeList[Bind]): Html[Bind] =
//    Html.Element(name, attributes, ReadList.Empty, children)

  def apply[BindF <: HList](attributes: Seq[Attr],
                            reads: ReadList[BindF]): Html[BindF] =
    Html.Element(name, attributes, reads, NodeList.Empty)

  // One argument
  def apply[Bind <: HList](children: NodeList[Bind]): Html[Bind] =
    Html.Element(name, Nil, ReadList.Empty, children)

//  def apply[BindF <: HList](reads: ReadList[BindF]): Html[BindF] =
//    Html.Element(name, Nil, reads, NodeList.Empty)

  def apply(attributes: Seq[Attr]): Html[HNil] =
    Html.Element(name, attributes, ReadList.Empty, NodeList.Empty)
}
