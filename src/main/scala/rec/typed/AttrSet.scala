package rec.typed

object AttrSet extends SKSet[Attribute] {
  type AttrSet[A] = this.SKSet[A]

  trait Syntax {
    implicit def applyAttrSet[A <: String with Singleton](
        c: Attribute[A]): AttrSet[A] =
      AttrSet.apply(c)
  }
}
