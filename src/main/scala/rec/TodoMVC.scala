package rec

import org.scalajs.dom
import org.scalajs.dom.KeyboardEvent
import rec.Attr.Attribute
import rec.syntax.DSL._
import shapeless._
import shapeless.record._

class TodoMVC extends RecHtmlApp {
  def isEnter(ev: dom.Event) = ev.asInstanceOf[KeyboardEvent].key == "Enter"

  case class Todo(content: String, checked: Boolean)

  type TodoSubmit    = ("in" ->> String) :: HNil
  type CheckedSubmit = ("checked" ->> Boolean) :: HNil

  def viewTodo[R](id: Int,
                  todo: Todo,
                  onUpdate: (Int, Option[Todo]) => Html[_]): Html[_] = {
    Html.fix[CheckedSubmit] { withReader =>
      val checkClicked = withReader(onclick) { (data, _) =>
        onUpdate(id, Some(todo.copy(checked = data("checked"))))
      }

      /*
      <li><div class="view">
        <input class="toggle" type="checkbox" checked=todo.checked checkClicked checked=#checked/>
        <label>todo.content</label>
        <button class="destroy" onclick(_ => onUpdate(id, None)></button>
      </div></li>
       */
      li(
        Nil,
        RNil,
        div(
          `class`("view"),
          RNil,
          input(
            `class`("toggle") :: tpe("checkbox") :: checked(todo.checked) :: checkClicked,
            field("checked", "checked".is[Boolean]),
            NNil) ::
            label(Nil, RNil, text(todo.content)) ::
            button(`class`("destroy") :: onclick(_ => onUpdate(id, None)),
                   RNil,
                   NNil)
        ) :: NNil
      )
    }
  }

  def makeToggleAll(todos: List[Todo]) = Html.fix[CheckedSubmit] { withReader =>
    /*
    <input id="toggle-all" class="toggle-all" type="checkbox" checked=#checked
      withReader(onclick) { (data, _) =>
        html(todos.map(_.copy(checked = data.checked)))
      } />
     */
    input(
      id("toggle-all") ::
        `class`("toggle-all") ::
        tpe("checkbox") ::
        withReader(onclick) { (data, _) =>
        html(todos.map(_.copy(checked = data("checked"))))
      },
      field("checked", "checked".is[Boolean]),
      NNil
    )
  }

  def mkTodoSubmit(todos: List[Todo], resetSubmission: Boolean) =
    Html.fix[TodoSubmit] { withReader =>
      val clk = withReader(onkeyup) { (data, ev) =>
        if (isEnter(ev)) html(Todo(data("in"), false) :: todos, true)
        else html(todos)
      }
      val reset = if (resetSubmission) List(value("")) else Nil

      /*
      <input class="new-todo" placeholder="enter todo" autofocus clk
        reset value=#in />
       */
      input(
        `class`("new-todo") :: placeholder("Enter todo...") :: autofocus :: clk :: reset,
        field("value", "in".is[String]),
        NNil)
    }

  def mkTodoViews(todos: List[Todo]): List[Html[HNil]] = todos.zipWithIndex.map {
    case (t, i) =>
      Html.ignore(viewTodo(i, t, {
        case (i, Some(todo)) => html(todos.updated(i, todo))
        case (i, _)          => html(todos.patch(i, Nil, 1)) // remove at idx i
      }))
  }

  def html(todos: List[Todo], resetSubmission: Boolean = false): Html[_] =
    /*
    <div>
      <header class="header">
        <h1>todos</h1>
        mkTodoSubmit(todos, resetSubmission)
      </header>
      <section class="main">
        maketoggleAll(todos)
        <label for="toggle-all">Mark all as complete</label>
        <ul class="todo-list">
          mkTodoViews(todos)
        </ul>
      </section
    </div>
     */
    div(
      Nil,
      RNil,
      header(`class`("header"),
             RNil,
             h1(Nil, RNil, text("todos")) :: mkTodoSubmit(
               todos,
               resetSubmission) ) ::
        section(
        `class`("main"),
        RNil,
        makeToggleAll(todos) ::
          label(Attribute("for", "toggle-all"),
                RNil,
                text("Mark all as complete")) ::
          ul(`class`("todo-list"), RNil, mkTodoViews(todos) ::: NNil)
      )
    )

  def html: Html[_] = html(Nil)
}
