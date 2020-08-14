package rec.syntax

import rec.Html.{Field, Text}
import rec.NodeList.NodeList
import rec.ReadList.ReadList
import rec._
import shapeless._
import shapeless.labelled.FieldType

import scala.language.implicitConversions

object DSL extends Tags with Attributes.Attrs with Events {
  type ->>[K, V] = FieldType[K, V]

  val RNil: ReadList[HNil] = ReadList.Empty
  val NNil: NodeList[HNil] = NodeList.Empty

  case class FieldSelector[R](fieldName: String)
  implicit class FieldSelectorMaker[K <: Singleton with String](str: K)(
      implicit witness: Witness.Aux[K]) {
    def is[V]: FieldSelector[FieldType[K, V] :: HNil] =
      FieldSelector[FieldType[K, V] :: HNil](str)
  }

  val text: String => Html[HNil] = str => Text(str)
  def field[R](attribute: String, field: FieldSelector[R]): Field[R] =
    Field[R](attribute, field.fieldName)

  implicit def toList[R](attr: Attr): List[Attr] = List(attr)
  implicit def toNodeList[R <: HList](html: Html[R])(
      implicit recordConcat: RecordConcat[HNil, R, R]): NodeList[R] =
    NodeList.RecordList[R](List(html))

  implicit def toReadList[R <: HList](field: Field[R])(
      implicit recordConcat: RecordConcat[HNil, R, R]): ReadList[R] =
    ReadList.RecordList[R](List(field))
}
