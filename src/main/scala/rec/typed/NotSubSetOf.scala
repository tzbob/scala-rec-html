package rec.typed

import rec.typed.SubsetOf.⊂

import scala.annotation.implicitAmbiguous

object NotSubSetOf {
  trait NotSubSetOf[E, Set]
  type ⊄[A, B] = NotSubSetOf[A, B]
  implicit def isNotSubSetOf[Left, Right]: NotSubSetOf[Left, Right] = null
  @implicitAmbiguous(
    "{${Left}} ⊂ {${Right}}: ${Left} already appears in ${Right}")
  implicit def isSubSetOf1[Left, Right](
      implicit ev: Left ⊂ Right): NotSubSetOf[Left, Right] = null
  implicit def isSubSetOf2[Left, Right](
      implicit ev: Left ⊂ Right): NotSubSetOf[Left, Right] = null
}

