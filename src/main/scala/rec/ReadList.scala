package rec

import rec.Html.Field
import shapeless.HList

object ReadList extends RecordList[Field] {
  type ReadList[R <: HList] = this.RecordList[R]
}
