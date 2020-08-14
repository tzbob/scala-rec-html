package rec.typed

import scala.annotation.implicitNotFound

object SubsetOf {
  @implicitNotFound("{${Left}} ⊄ {${Right}}")
  trait SubsetOf[Left, Right]
  type ⊂[A, B] = SubsetOf[A, B]
  implicit def isSubSetOf[Left, Right](
      implicit ev: Right with "" <:< Left): Left SubsetOf Right =
    null
}
