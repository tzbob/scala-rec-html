package rec.containerread.syntax

import rec.containerread.Attribute

// FROM https://github.com/lihaoyi/scalatags/blob/8f3ab14433f44f7a568e91ef806c08c6a1855861/scalatags/src/scalatags/generic/Attrs.scala

trait Events {
  private def event(key: String): Attribute.EventBindMaker =
    new Attribute.EventBindMaker(key)

  /**
    * The keydown event is raised when the user presses a keyboard key.
    *
    * MDN
    */
  lazy val onkeydown = event("keydown")

  /**
    * The keyup event is raised when the user releases a key that's been pressed.
    *
    * MDN
    */
  lazy val onkeyup = event("keyup")

  /**
    * The keypress event should be raised when the user presses a key on the keyboard.
    * However, not all browsers fire keypress events for certain keys.
    *
    * Webkit-based browsers (Google Chrome and Safari, for example) do not fire keypress events on the arrow keys.
    * Firefox does not fire keypress events on modifier keys like SHIFT.
    *
    * MDN
    */
  lazy val onkeypress = event("keypress")

  /**
    * The click event is raised when the user clicks on an element. The click
    * event will occur after the mousedown and mouseup events.
    *
    * MDN
    */
  lazy val onclick = event("click")

  /**
    * The dblclick event is fired when a pointing device button (usually a
    * mouse button) is clicked twice on a single element.
    *
    * MDN
    */
  lazy val ondblclick = event("dblclick")

  /**
    * Script to be run when an element is dragged
    */
  val ondrag = event("drag")

  /**
    * Script to be run at the end of a drag operation
    */
  lazy val ondragend = event("dragend")

  /**
    * Script to be run when an element has been dragged to a valid drop target
    */
  lazy val ondragenter = event("dragenter")

  /**
    * Script to be run when an element leaves a valid drop target
    */
  lazy val ondragleave = event("dragleave")

  /**
    * Script to be run when an element is being dragged over a valid drop target
    */
  lazy val ondragover = event("dragover")

  /**
    * Script to be run at the start of a drag operation
    */
  lazy val ondragstart = event("dragstart")

  /**
    * Script to be run when dragged element is being dropped
    */
  lazy val ondrop = event("drop")

  /**
    * The mousedown event is raised when the user presses the mouse button.
    *
    * MDN
    */
  lazy val onmousedown = event("mousedown")

  /**
    * The mousemove event is raised when the user moves the mouse.
    *
    * MDN
    */
  lazy val onmousemove = event("mousemove")

  /**
    * The mouseout event is raised when the mouse leaves an element (e.g, when
    * the mouse moves off of an image in the web page, the mouseout event is
    * raised for that image element).
    *
    * MDN
    */
  lazy val onmouseout = event("mouseout")

  /**
    * The mouseover event is raised when the user moves the mouse over a
    * particular element.
    *
    * MDN
    */
  lazy val onmouseover = event("mouseover")

  /**
    * The mouseup event is raised when the user releases the mouse button.
    *
    * MDN
    */
  lazy val onmouseup = event("mouseup")

  /**
    * Specifies the function to be called when the window is scrolled.
    *
    * MDN
    */
  lazy val onscroll = event("scroll")

  /**
    * Fires when the mouse wheel rolls up or down over an element
    */
  lazy val onwheel = event("wheel")

  /**
    * The blur event is raised when an element loses focus.
    *
    * MDN
    */
  lazy val onblur = event("blur")

  /**
    * The change event is fired for input, select, and textarea elements
    * when a change to the element's value is committed by the user.
    *
    * MDN
    */
  lazy val onchange = event("change")

  /**
    * The focus event is raised when the user sets focus on the given element.
    *
    * MDN
    */
  lazy val onfocus = event("focus")

  /**
    * The select event only fires when text inside a text input or textarea is
    * selected. The event is fired after the text has been selected.
    *
    * MDN
    */
  lazy val onselect = event("select")

  /**
    * The submit event is raised when the user clicks a submit button in a form
    * (<input type="submit"/>).
    *
    * MDN
    */
  lazy val onsubmit = event("submit")

  /**
    * The reset event is fired when a form is reset.
    *
    * MDN
    */
  lazy val onreset = event("reset")

  /**
    * Script to be run when a context menu is triggered
    */
  lazy val oncontextmenu = event("contextmenu")

  /**
    * Script to be run when an element gets user input
    */
  lazy val oninput = event("input")

  /**
    * Script to be run when an element is invalid
    */
  lazy val oninvalid = event("invalid")

  /**
    * Fires when the user writes something in a search field (for <input="search">)
    */
  lazy val onsearch = event("search")

  /**
    * The load event fires at the end of the document loading process. At this
    * point, all of the objects in the document are in the DOM, and all the
    * images and sub-frames have finished loading.
    *
    * MDN
    */
  lazy val onload = event("load")

  /**
    * Script to be run after the document is printed
    */
  lazy val onafterprint = event("afterprint")

  /**
    * Script to be run before the document is printed
    */
  lazy val onbeforeprint = event("beforeprint")

  /**
    * Script to be run when the document is about to be unloaded
    */
  lazy val onbeforeunload = event("beforeunload")

  /**
    * Script to be run when there has been changes to the anchor part of the a URL
    */
  lazy val onhashchange = event("hashchange")

  /**
    * Script to be run when the message is triggered
    */
  lazy val onmessage = event("message")

  /**
    * Script to be run when the browser starts to work offline
    */
  lazy val onoffline = event("offline")

  /**
    * Script to be run when the browser starts to work online
    */
  lazy val ononline = event("online")

  /**
    * Script to be run when a user navigates away from a page
    */
  lazy val onpagehide = event("pagehide")

  /**
    * Script to be run when a user navigates to a page
    */
  lazy val onpageshow = event("pageshow")

  /**
    * Script to be run when the window's history changes
    */
  lazy val onpopstate = event("popstate")

  /**
    * Fires when the browser window is resized
    */
  lazy val onresize = event("resize")

  /**
    * Script to be run when a Web Storage area is updated
    */
  lazy val onstorage = event("storage")

  /**
    * Fires once a page has unloaded (or the browser window has been closed)
    */
  lazy val onunload = event("unload")
}
