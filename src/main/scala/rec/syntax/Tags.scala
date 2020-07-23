package rec.syntax

import rec.{Attr, Html, NodeList, ReadList}
import rec.Html._

trait Tags {
  private type HtmlMaker[R, CR] =
    (Seq[Attr], ReadList[R], NodeList[CR]) => Html[R with CR]
  private def mkTag[R, CR](str: String): HtmlMaker[R, CR] =
    Element[R, CR](str, _, _, _)

  // From https://github.com/lihaoyi/scalatags/blob/master/scalatags/src/scalatags/vdom/Tags.scala
  def html[R, CR]       = mkTag[R, CR]("html")
  def head[R, CR]       = mkTag[R, CR]("head")
  def base[R, CR]       = mkTag[R, CR]("base")
  def link[R, CR]       = mkTag[R, CR]("link")
  def meta[R, CR]       = mkTag[R, CR]("meta")
  def script[R, CR]     = mkTag[R, CR]("script")
  def body[R, CR]       = mkTag[R, CR]("body")
  def h1[R, CR]         = mkTag[R, CR]("h1")
  def h2[R, CR]         = mkTag[R, CR]("h2")
  def h3[R, CR]         = mkTag[R, CR]("h3")
  def h4[R, CR]         = mkTag[R, CR]("h4")
  def h5[R, CR]         = mkTag[R, CR]("h5")
  def h6[R, CR]         = mkTag[R, CR]("h6")
  def header[R, CR]     = mkTag[R, CR]("header")
  def footer[R, CR]     = mkTag[R, CR]("footer")
  def p[R, CR]          = mkTag[R, CR]("p")
  def hr[R, CR]         = mkTag[R, CR]("hr")
  def pre[R, CR]        = mkTag[R, CR]("pre")
  def blockquote[R, CR] = mkTag[R, CR]("blockquote")
  def ol[R, CR]         = mkTag[R, CR]("ol")
  def ul[R, CR]         = mkTag[R, CR]("ul")
  def li[R, CR]         = mkTag[R, CR]("li")
  def dl[R, CR]         = mkTag[R, CR]("dl")
  def dt[R, CR]         = mkTag[R, CR]("dt")
  def dd[R, CR]         = mkTag[R, CR]("dd")
  def figure[R, CR]     = mkTag[R, CR]("figure")
  def figcaption[R, CR] = mkTag[R, CR]("figcaption")
  def div[R, CR]        = mkTag[R, CR]("div")
  def a[R, CR]          = mkTag[R, CR]("a")
  def em[R, CR]         = mkTag[R, CR]("em")
  def strong[R, CR]     = mkTag[R, CR]("strong")
  def small[R, CR]      = mkTag[R, CR]("small")
  def s[R, CR]          = mkTag[R, CR]("s")
  def cite[R, CR]       = mkTag[R, CR]("cite")
  def code[R, CR]       = mkTag[R, CR]("code")
  def sub[R, CR]        = mkTag[R, CR]("sub")
  def sup[R, CR]        = mkTag[R, CR]("sup")
  def i[R, CR]          = mkTag[R, CR]("i")
  def b[R, CR]          = mkTag[R, CR]("b")
  def u[R, CR]          = mkTag[R, CR]("u")
  def span[R, CR]       = mkTag[R, CR]("span")
  def br[R, CR]         = mkTag[R, CR]("br")
  def wbr[R, CR]        = mkTag[R, CR]("wbr")
  def ins[R, CR]        = mkTag[R, CR]("ins")
  def del[R, CR]        = mkTag[R, CR]("del")
  def img[R, CR]        = mkTag[R, CR]("img")
  def iframe[R, CR]     = mkTag[R, CR]("iframe")
  def embed[R, CR]      = mkTag[R, CR]("embed")
  def `object`[R, CR]   = mkTag[R, CR]("object")
  def param[R, CR]      = mkTag[R, CR]("param")
  def video[R, CR]      = mkTag[R, CR]("video")
  def audio[R, CR]      = mkTag[R, CR]("audio")
  def source[R, CR]     = mkTag[R, CR]("source")
  def track[R, CR]      = mkTag[R, CR]("track")
  def canvas[R, CR]     = mkTag[R, CR]("canvas")
  def map[R, CR]        = mkTag[R, CR]("map")
  def area[R, CR]       = mkTag[R, CR]("area")
  def table[R, CR]      = mkTag[R, CR]("table")
  def caption[R, CR]    = mkTag[R, CR]("caption")
  def colgroup[R, CR]   = mkTag[R, CR]("colgroup")
  def col[R, CR]        = mkTag[R, CR]("col")
  def tbody[R, CR]      = mkTag[R, CR]("tbody")
  def thead[R, CR]      = mkTag[R, CR]("thead")
  def tfoot[R, CR]      = mkTag[R, CR]("tfoot")
  def tr[R, CR]         = mkTag[R, CR]("tr")
  def td[R, CR]         = mkTag[R, CR]("td")
  def th[R, CR]         = mkTag[R, CR]("th")
  def form[R, CR]       = mkTag[R, CR]("form")
  def fieldset[R, CR]   = mkTag[R, CR]("fieldset")
  def legend[R, CR]     = mkTag[R, CR]("legend")
  def label[R, CR]      = mkTag[R, CR]("label")
  def input[R, CR]      = mkTag[R, CR]("input")
  def button[R, CR]     = mkTag[R, CR]("button")
  def select[R, CR]     = mkTag[R, CR]("select")
  def datalist[R, CR]   = mkTag[R, CR]("datalist")
  def optgroup[R, CR]   = mkTag[R, CR]("optgroup")
  def option[R, CR]     = mkTag[R, CR]("option")
  def textarea[R, CR]   = mkTag[R, CR]("textarea")

  def section[R, CR] = mkTag[R, CR]("section")
  def nav[R, CR]     = mkTag[R, CR]("nav")
  def article[R, CR] = mkTag[R, CR]("article")
  def aside[R, CR]   = mkTag[R, CR]("aside")
  def address[R, CR] = mkTag[R, CR]("address")
  def menu[R, CR]    = mkTag[R, CR]("menu")
}
