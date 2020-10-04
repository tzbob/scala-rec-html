package rec.typed

import org.scalajs.dom

case class Attribute[Name <: String with Singleton](name: Name, attr: rec.Attr)

object Attribute {
  def attribute[Name <: String with Singleton](name: Name): String => Attribute[Name] =
    value => Attribute(name, rec.Attr.Attribute(name, value))

  def property[Name <: String with Singleton](name: Name): String => Attribute[Name] =
    value => Attribute(name, rec.Attr.Property(name, value))

  class EventBinder[Name <: String with Singleton](val name: Name) {
    def apply(value: dom.Event => Html[_, _]): Attribute[Name] = {
      val untypedF: dom.Event => rec.Html[_] = ev => value(ev).html
      Attribute(name, rec.Attr.EventBind(name, untypedF))
    }
  }

  def event[Name <: String with Singleton](name: Name): EventBinder[Name] =
    new EventBinder[Name](name)
}
