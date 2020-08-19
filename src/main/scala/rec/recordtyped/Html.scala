package rec.recordtyped

import org.scalajs.dom
import rec.syntax.EventBindMaker
import rec.{Attr, FromMap}
import shapeless.{HList, HNil}

object Html {
  type Html[R <: HList] = rec.Html[R]

  trait TagEventBindMaker[R <: HList] {
    def apply[A](binder: A => EventBindMaker)(
        f: (R, dom.Event) => Html[_]): (A => Attr)
  }

  def fix[R <: HList](f: TagEventBindMaker[R] => Html[R])(
      implicit fromMap: FromMap[R]): Html[HNil] = {
    rec.Html.fix[R] { withReader =>
      f(new TagEventBindMaker[R] {
        def apply[A](binder: A => EventBindMaker)(
            f: (R, dom.Event) => Html[_]): (A => Attr) = a => {
          withReader(binder(a))(f)
        }
      })
    }
  }
}
