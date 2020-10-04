package rec.containerread

import org.scalajs.dom
import rec.FromMap
import shapeless.{HList, HNil}

import scala.scalajs.js.|

sealed trait Attribute[+Use]

object Attribute {
  implicit class AttributeCombined[Use](left: Attribute[Use]) {
    def |+|(right: Attribute[Use]): Attribute[Use] = Combined(left, right)
  }

  case class AttributeImpl[Use](key: String, value: String)
      extends Attribute[Use]

  case class Property[Use](key: String, value: String | Boolean | Int)
      extends Attribute[Use]

  case class EventBind[Use](event: String, f: dom.Event => Html[Nothing, HNil])
      extends Attribute[Use]

  case class EventReadBind[Use <: HList](
      event: String,
      f: (Use, dom.Event) => Html[Nothing, HNil])(
      implicit val fromMap: FromMap[Use])
      extends Attribute[Use] {
    def execF(ev: dom.Event, map: Map[String, Any]): Html[Nothing, HNil] =
      fromMap(map) match {
        case None =>
          throw new RuntimeException(s"Cannot turn $map into readable data")
        case Some(value) => f(value, ev)
      }
  }

  case object Nil extends Attribute[Nothing]
  val empty: Attribute[Nothing] = Nil

  case class Combined[Use](left: Attribute[Use], right: Attribute[Use])
      extends Attribute[Use]

  def list[Use](attribute: Attribute[Use]): List[Attribute[Use]] =
    attribute match {
      case Combined(left, right) => list(left) ::: list(right)
      case Nil                   => List.empty
      case a                     => List(a)
    }

  class EventBindMaker(val key: String) {
    def readL[Use <: HList: FromMap](
        value: (Use, dom.Event) => Html[Nothing, HNil]): Attribute[Use] =
      EventReadBind(key, value)

    def read[Use <: HList: FromMap](
        value: Use => Html[Nothing, HNil]): Attribute[Use] =
      EventReadBind(key, (u, _) => value(u))

    def listen(value: dom.Event => Html[Nothing, HNil]): Attribute[Nothing] =
      EventBind(key, value)

    def as[Use <: HList: FromMap](
        value: => Html[Nothing, HNil]): Attribute[Nothing] =
      EventBind(key, _ => value)
  }
}
