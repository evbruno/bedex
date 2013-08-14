package bedex.view

import bedex.biz.Displayable
import scalafx.util.StringConverter

class DisplayableObjectReadonlyConverter[T <: Displayable] extends StringConverter[T] {
  def toString(obj: T) = obj.display
  def fromString(obj: String) = throw new RuntimeException
}