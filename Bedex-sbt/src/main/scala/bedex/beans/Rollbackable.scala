package bedex.beans

import javafx.beans.value.WritableObjectValue
import javafx.beans.value.ObservableValue
import javafx.beans.value.ObservableValue

trait Rollbackable[T] extends WritableObjectValue[T] {

  protected var originalValue: Option[T] = None

  def isDirty(): Boolean = !originalValue.isEmpty

  def rollback() = {
    originalValue match {
      case Some(v) => {
        set(v)
        originalValue = None
      }
      case None =>
    }
  }

  def commit() = originalValue = None

}