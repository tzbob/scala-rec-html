package rec.typed

trait RecHtmlApp extends rec.RecHtmlApp {
  def typedHtml: Html[_, _]
  def html: rec.Html[_] = typedHtml.html
}
