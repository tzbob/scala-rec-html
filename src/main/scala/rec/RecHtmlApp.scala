package rec

import org.scalajs.dom

trait RecHtmlApp {
  def run(html: Html[_], element: dom.Element): Unit = {
    lazy val domPatcher: DomPatcher = new DomPatcher(
      Html.toVNode(html, vn => domPatcher.applyNewState(vn)),
      element)
    domPatcher
    ()
  }

  def main(args: Array[String]): Unit = {
    run(html, dom.document.getElementById("app"))
    println("Starting...")
  }

  def html: Html[_]
}
