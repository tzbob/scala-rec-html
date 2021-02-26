package rec.allowconflicts.syntax

import rec.isolate.Attribute
import shapeless._
import shapeless.labelled.FieldType

import scala.language.implicitConversions

object DSL extends rec.isolate.syntax.DSL {
  object allowconflictsSyntax { implicit def listToHead[A](s: Seq[A]): A = s.head }

  implicit class FieldSelectorMaker[K <: Singleton with String](str: K)(
      implicit witness: Witness.Aux[K]) {
    def is[V]: FieldSelector[FieldType[K, Seq[V]] :: HNil] =
      FieldSelector[FieldType[K, Seq[V]] :: HNil](str)
  }
}
