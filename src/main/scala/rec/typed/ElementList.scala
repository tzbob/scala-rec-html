package rec.typed

import rec.typed.NoConstraint.NoConstraint
import shapeless.HList

object ElementList extends SKRecordList[Html, NoConstraint] {
  type ElementList[Set, R <: HList] = this.SKRecordList[Set, R]

  trait Syntax {
    implicit def applyHtml[A <: String with Singleton, Read <: HList](
        c: Html[A, Read]): ElementList[A, Read] = SKRecordList[A, Read](List(c))
  }
}
