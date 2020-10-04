package rec.typed

trait RecHtmlApp extends rec.RecHtmlApp {
  def main: Html[_, _]
  def html: rec.Html[_] = main.html
}
