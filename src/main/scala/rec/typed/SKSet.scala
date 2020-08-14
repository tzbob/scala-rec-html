package rec.typed

import rec.typed.NotSubSetOf.⊄

trait SKSet[C[_ <: String with Singleton]] {
  case class SKSet[S](private val set: Set[Any]) {
    def &[A <: String with Singleton](a: C[A])(
        implicit ev: A ⊄ S): SKSet[S with A] =
      SKSet(set + a)
    def toSet[B](f: C[_] => B): Set[B] = set.map(x => f(x.asInstanceOf[C[_]]))
  }
  implicit def apply[A <: String with Singleton](c: C[A]): SKSet[A] =
    SKSet[A](Set(c))

  val empty: SKSet[""] = SKSet[""](Set.empty)
}
