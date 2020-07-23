package rec

import org.scalajs.dom
import org.scalajs.dom.KeyboardEvent
import rec.Attr.Attribute
import rec.syntax.DSL._

import scala.scalajs.js

object TodoMVC extends RecHtmlApp {
  def isEnter(ev: dom.Event) = ev.asInstanceOf[KeyboardEvent].key == "Enter"

  case class Todo(content: String, checked: Boolean)
  trait TodoSubmit    extends js.Object { val in: String       = js.native }
  trait CheckedSubmit extends js.Object { val checked: Boolean = js.native }

  trait TodoChange extends js.Object {
    val content: String  = js.native
    val checked: Boolean = js.native
  }

  def viewTodo[R](id: Int,
                  todo: Todo,
                  onUpdate: (Int, Option[Todo]) => Html[_]): Html[_] = {
    Html.fix[CheckedSubmit] { withReader =>
      val checkClicked = withReader(onclick) { (data, _) =>
        onUpdate(id, Some(todo.copy(checked = data.checked)))
      }

      li(
        Nil,
        RNil,
        div(
          `class`("view"),
          RNil,
          input(
            `class`("toggle") :: tpe("checkbox") :: checked(todo.checked) :: checkClicked,
            field[{ val checked: Boolean }]("checked", "checked"),
            NNil) ::
            label(Nil, RNil, text(todo.content)) ::
            button(`class`("destroy") :: onclick(_ => onUpdate(id, None)),
                   RNil,
                   NNil)
        )
      )
    }
  }

  def makeToggleAll(todos: List[Todo]) = Html.fix[CheckedSubmit] { withReader =>
    input(
      id("toggle-all") ::
        `class`("toggle-all") ::
        tpe("checkbox") ::
        withReader(onclick) { (data, _) =>
        html(todos.map(_.copy(checked = data.checked)))
      },
      field[CheckedSubmit]("checked", "checked"),
      NNil
    )
  }

  def mkTodoSubmit(todos: List[Todo], resetSubmission: Boolean) =
    Html.fix[TodoSubmit] { withReader =>
      val clk = withReader(onkeyup) { (data, ev) =>
        if (isEnter(ev)) html(Todo(data.in, false) :: todos, true)
        else html(todos)
      }
      val reset = if (resetSubmission) List(value("")) else Nil
      input(
        `class`("new-todo") :: placeholder("Enter todo...") :: autofocus :: clk :: reset,
        field[TodoSubmit]("value", "in"),
        NNil)
    }

  def mkTodoViews(todos: List[Todo]) = todos.zipWithIndex.map {
    case (t, i) =>
      Html.ignore(viewTodo(i, t, {
        case (i, Some(todo)) => html(todos.updated(i, todo))
        case (i, _)          => html(todos.patch(i, Nil, 1)) // remove at idx i
      }))
  }

  def html(todos: List[Todo], resetSubmission: Boolean = false): Html[_] =
    div(
      Nil,
      RNil,
      header(`class`("header"),
             RNil,
             h1(Nil, RNil, text("todos")) :: mkTodoSubmit(todos,
                                                          resetSubmission)) ::
        section(
        `class`("main"),
        RNil,
        makeToggleAll(todos) ::
          label(Attribute("for", "toggle-all"),
                RNil,
                text("Mark all as complete")) ::
          ul(`class`("todo-list"),
             RNil,
             li(Nil, RNil, mkTodoViews(todos) ::: NNil))
      )
    )

  def html: Html[_] = html(Nil)
}
