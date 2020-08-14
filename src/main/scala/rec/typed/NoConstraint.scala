package rec.typed

object NoConstraint {
  trait NoConstraint[A, B]
  implicit def alwaysThere[A, B]: A NoConstraint B = null
}
