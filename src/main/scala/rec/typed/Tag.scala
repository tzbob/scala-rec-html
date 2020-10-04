package rec.typed

import rec.{NodeList, ReadList, RecordConcat}
import rec.typed.AttrSet.AttrSet
import rec.typed.FieldList.FieldList
import rec.typed.ElementList.ElementList
import rec.typed.SubsetOf.⊂
import shapeless.HList

case class Tag[Name <: String with Singleton, AcceptedAttrs, AcceptedFields, AcceptedChildren](
    name: Name) {
  def apply[FieldRead <: HList,
            ChildrenRead <: HList,
            Read <: HList,
            GivenAttrs,
            GivenFields,
            GivenChildren](attributes: AttrSet[GivenAttrs],
                           fields: FieldList[GivenFields, FieldRead],
                           children: ElementList[GivenChildren, ChildrenRead])(
      implicit attrsOK: GivenAttrs ⊂ AcceptedAttrs,
      fieldsOK: GivenFields ⊂ AcceptedFields,
      childrenOK: GivenChildren ⊂ AcceptedChildren,
      recordConcat: RecordConcat[FieldRead, ChildrenRead, Read]
  ): Html[Name, Read] =
    Html(
      rec.Html.Element(name,
                       attributes.toSet(_.attr).toList,
                       fields.toRecordList(ReadList)(_.field),
                       children.toRecordList(NodeList)(_.html)))
}

object Tag {
  case class Make[Name <: String with Singleton](name: Name) {
    def apply[AcceptedAttrs, AcceptedFields, AcceptedChildren]
      : Tag[Name, AcceptedAttrs, AcceptedFields, AcceptedChildren] =
      Tag(name)
  }
}
