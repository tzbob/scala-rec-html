package rec.syntax

import rec.{Attr, Html, NodeList, ReadList}
import rec.Html.{Field, Text}

import scala.language.implicitConversions

object DSL extends Tags with Attributes.Attrs with Events {
  val RNil: ReadList[{}] = ReadList.Empty
  val NNil: NodeList[{}] = NodeList.Empty

  val text: String => Html[{}]               = str => Text(str)
  def field[R]: (String, String) => Field[R] = Field[R](_, _)

  implicit def toList[R](attr: Attr): List[Attr] = List(attr)
  implicit def toNodeList[R](html: Html[R]): NodeList[R with AnyRef] =
    NodeList.NCons(html, NodeList.Empty).widen
  implicit def toNodeListN[R](html: List[Html[R]]): NodeList[{
    def list: List[R]
  }] =
    NodeList.ListCons(html, NodeList.Empty).widen
  implicit def toReadList[R](field: Field[R]): ReadList.Cons[R, {}] =
    ReadList.Cons(field, ReadList.Empty)
}
