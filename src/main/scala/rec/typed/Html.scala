package rec.typed

import org.scalajs.dom
import rec.typed.Attribute.EventBinder
import rec.{Attr, FromMap, syntax}
import shapeless.{HList, HNil}

case class Html[Set, Read](html: rec.Html[Read]) {
  type Children = Set
}

object Html {
  def ignore[S, R](html: Html[S, R]): Html[S, HNil] =
    html.asInstanceOf[Html[S, HNil]]

  trait EventBinderWrapper[R <: HList] {
    def apply[Name <: String with Singleton](binder: EventBinder[Name])(
        fun: (R, dom.Event) => Html[_, _]): Attribute[Name]
  }

  def apply[R <: HList]() = Reader[R]()

  case class Reader[R <: HList]() {
    def fix[S](f: EventBinderWrapper[R] => Html[S, R])(
        implicit fromMap: FromMap[R]): Html[S, HNil] = {
      val untypedHtml = rec.Html.fix[R] { withReader: (syntax.EventBindMaker => (
          (R, dom.Event) => rec.Html[_]) => Attr) =>
        val typedReader = new EventBinderWrapper[R] {
          def apply[Name <: String with Singleton](binder: EventBinder[Name])(
              fun: (R, dom.Event) => Html[_, _]): Attribute[Name] = {
            val untypedReader =
              withReader(new syntax.EventBindMaker(binder.name))
            val untypedF: (R, dom.Event) => rec.Html[_] =
              (r, ev) => fun(r, ev).html
            val untypedAttr = untypedReader(untypedF)
            Attribute(binder.name, untypedAttr)
          }
        }
        val typedHtml = f(typedReader)
        typedHtml.html
      }
      Html(untypedHtml)
    }
  }

  def fix[Name <: String with Singleton, S, R <: HList](
      f: (EventBinder[Name] => ((R, dom.Event) => Html[_, _]) => Attribute[Name]) => Html[
        S,
        R])(implicit fromMap: FromMap[R]): Html[S, HNil] = {
    val untypedHtml = rec.Html.fix[R] { withReader: (syntax.EventBindMaker => (
        (R, dom.Event) => rec.Html[_]) => Attr) =>
      val typedReader: EventBinder[Name] => (
          (R, dom.Event) => Html[_, _]) => Attribute[Name] = binder =>
        fun => {
          val untypedReader = withReader(new syntax.EventBindMaker(binder.name))
          val untypedF: (R, dom.Event) => rec.Html[_] =
            (r, ev) => fun(r, ev).html
          val untypedAttr = untypedReader(untypedF)
          Attribute(binder.name, untypedAttr)
      }
      val typedHtml = f(typedReader)
      typedHtml.html
    }
    Html(untypedHtml)
  }
}
