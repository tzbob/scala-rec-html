package rec.recordtyped

import rec.Html.Field
import rec.recordtyped.ChildrenList.ChildrenList
import rec.recordtyped.FieldList.FieldList
import rec.{Html, NodeList, ReadList, RecordConcat}
import shapeless.HList

case class Tag[A, F, C](name: String,
                        availableAttrs: A,
                        availableFields: F,
                        availableChildren: C) {
  def apply[FieldRead <: HList, ChildrenRead <: HList, Read <: HList](
      attributes: List[A => rec.Attr],
      fields: FieldList[F, FieldRead],
      children: ChildrenList[C, ChildrenRead])(
      implicit recordConcat: RecordConcat[FieldRead, ChildrenRead, Read])
    : rec.Html[Read] =
    rec.Html.Element[FieldRead, ChildrenRead, Read](
      name,
      attributes.map(_ apply availableAttrs),
      fields.toRecordList[Field](ReadList) { f =>
        f(availableFields)
      },
      children.toRecordList[Html](NodeList) { f =>
        f(availableChildren)
      },
    )
}
