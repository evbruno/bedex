package bedex.view

import scalafx.util.StringConverter

class SomeConverter[T](prefix: String = "") extends StringConverter[Option[T]] {

  def toString(obj: Option[T]) = obj match {
    case Some(value) => prefix + value.toString
    case None => prefix + "None"
  }

  def fromString(obj: String) = throw new RuntimeException

}

class AnyConverter[T](to: T => String) extends StringConverter[T] {

  def toString(obj: T) = to(obj)

  def fromString(obj: String) = throw new RuntimeException

}