package rec.isolate.examples

import org.scalajs.dom
import org.scalajs.dom.KeyboardEvent
import rec.isolate.syntax.DSL._
import rec.isolate.{Attribute, Html, RecHtmlApp}
import shapeless._
import shapeless.record._

object TodoMVC extends RecHtmlApp {
  def isEnter(ev: dom.Event) = ev.asInstanceOf[KeyboardEvent].key == "Enter"

  case class Todo(content: String, checked: Boolean)

  type TodoSubmit    = ("in" ->> String) :: HNil
  type CheckedSubmit = ("checked" ->> Boolean) :: HNil

  def todoListItem[R](id: Int,
                      todo: Todo,
                      onUpdate: (Int, Option[Todo]) => Html[Nothing, HNil]): Html[Nothing, HNil] =
    Html.html {
      val checkClicked = onclick.read[CheckedSubmit] { data =>
        onUpdate(id, Some(todo.copy(checked = data("checked"))))
      }

      li(
        div(
          cls("view"),
          input(cls("toggle") |+| tpe("checkbox") |+| checked(todo.checked) |+| checkClicked |+|
            F.checked("checked")) |+|
            label(text(todo.content)) |+|
            button(cls("destroy") |+| onclick.as(onUpdate(id, None)))
        ))
    }

  def toggleAllButton(todos: List[Todo]): Html[Nothing, HNil] = Html.html {
    input(
      id("toggle-all") |+|
        cls("toggle-all") |+|
        tpe("checkbox") |+|
        onclick.read[CheckedSubmit] { data =>
          html(todos.map(_.copy(checked = data("checked"))))
        } |+|
        F.checked("checked")
    )
  }

  def submissionBox(todos: List[Todo], resetSubmission: Boolean): Html[Nothing, HNil] =
    Html.html {
      val resetValue = if (resetSubmission) value("") else Attribute.Nil
      val onEnterSubmitTodo = onkeyup.readL[TodoSubmit] { (data, ev) =>
        if (isEnter(ev)) html(Todo(data("in"), false) :: todos, true)
        else html(todos)
      }
      input(
        cls("new-todo") |+|
          placeholder("Enter todo...") |+|
          autofocus |+|
          F.value("in") |+|
          onEnterSubmitTodo |+|
          resetValue
      )
    }

  def mkTodoViews(todos: List[Todo]): Html[Nothing, HNil] =
    Html.fromSeq(todos.zipWithIndex.map {
      case (t, i) =>
        todoListItem(i, t, {
          case (i, Some(todo)) => html(todos.updated(i, todo))
          case (i, _)          => html(todos.patch(i, Nil, 1)) // remove at idx i
        })
    })

  def html(todos: List[Todo], resetSubmission: Boolean = false) =
    div(
      header(cls("header"), h1(text("todos")) |+| submissionBox(todos, resetSubmission)) |+|
        section(
          cls("main"),
          toggleAllButton(todos) |+|
            label(Attribute.AttributeImpl("for", "toggle-all"), text("Mark all as complete")) |+|
            ul(cls("todo-list"), mkTodoViews(todos))
        )
    )

  val main: Html[Nothing, HNil] = html(List.empty)
}
