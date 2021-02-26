package rec.isolate

import org.scalajs.dom
import rec.{FromMap, RecordConcat}
import shapeless.{HList, HNil}

import scala.scalajs.js.|

sealed trait Attribute[+Use, Bind]

object Attribute {
  implicit class AttributeCombined[Use, BindL <: HList](left: Attribute[Use, BindL]) {
    def |+|[BindR <: HList, Bind <: HList](right: Attribute[Use, BindR])(
        implicit recordConcat: RecordConcat[BindL, BindR, Bind]): Attribute[Use, Bind] =
      Combined(left, right)
  }

  case class AttributeImpl[Use](key: String, value: String)            extends Attribute[Use, HNil]
  case class Property[Use](key: String, value: String | Boolean | Int) extends Attribute[Use, HNil]

  case class EventBind[Use](event: String, f: dom.Event => Html[Nothing, HNil])
      extends Attribute[Use, HNil]

  case class EventReadBind[Use <: HList](event: String, f: (Use, dom.Event) => Html[Nothing, HNil])(
      implicit val fromMap: FromMap[Use])
      extends Attribute[Use, HNil] {
    def execF(ev: dom.Event, map: Map[String, Any]): Html[Nothing, HNil] = {
      fromMap(map) match {
        case None =>
          println(s"ev $ev event $event")
          throw new RuntimeException(s"Cannot turn $map into readable data")
        case Some(value) =>
          f(value, ev)
      }
    }
  }

  case class Field[Bind](key: String, fieldName: String) extends Attribute[Nothing, Bind]

  case object Nil extends Attribute[Nothing, HNil]
  val empty: Attribute[Nothing, HNil] = Nil

  case class Combined[Use, BindL <: HList, BindR <: HList, Bind <: HList](
      left: Attribute[Use, BindL],
      right: Attribute[Use, BindR])(implicit recordConcat: RecordConcat[BindL, BindR, Bind])
      extends Attribute[Use, Bind]

  def list(attribute: Attribute[_, _]): List[Attribute[_, _]] =
    attribute match {
      case Combined(left, right) => list(left) ::: list(right)
      case Nil                   => List.empty
      case a                     => List(a)
    }

  class EventBindMaker(val key: String) {
    def readL[Use <: HList: FromMap](
        value: (Use, dom.Event) => Html[Nothing, HNil]): Attribute[Use, HNil] =
      EventReadBind(key, value)

    def read[Use <: HList: FromMap](value: Use => Html[Nothing, HNil]): Attribute[Use, HNil] =
      EventReadBind(key, (u, _) => value(u))

    def listen(value: dom.Event => Html[Nothing, HNil]): Attribute[Nothing, HNil] =
      EventBind(key, value)

    def as[Use <: HList: FromMap](value: => Html[Nothing, HNil]): Attribute[Nothing, HNil] =
      EventBind(key, _ => value)
  }
}
