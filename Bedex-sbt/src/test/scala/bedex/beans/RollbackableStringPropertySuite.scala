package bedex.beans

import org.mockito.Matchers
import org.mockito.Matchers.anyObject
import org.mockito.Matchers.anyString
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.spy
import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import org.mockito.Mockito

class RollbackableStringPropertySuite extends FunSuite with MockitoSugar {

  test("Property isn't dirty") {
    val prop = new RollbackableStringProperty("hi")

    assert(!prop.isDirty)
    assert(prop.get === "hi")
  }

  test("Property isDirty") {
    val prop = new RollbackableStringProperty("hi")
    prop set "there"

    assert(prop.isDirty)
    assert(prop.get === "there")
  }

  test("Property updating multiple times") {
    val prop = new RollbackableStringProperty("hi")

    prop set "there"
    assert(prop.isDirty)
    assert(prop.get === "there")

    prop set "dude"
    assert(prop.isDirty)
    assert(prop.get === "dude")
  }

  test("Property isn't dirty after rollback") {
    val prop = new RollbackableStringProperty("hi")
    prop set "there"
    prop rollback

    assert(!prop.isDirty)
    assert(prop.get === "hi")
  }

  test("Property isn't dirty after commit") {
    val prop = new RollbackableStringProperty("hi")
    prop set "there"
    prop commit

    assert(!prop.isDirty)
    assert(prop.get === "there")
  }

  class NoOpListener extends ChangeListener[String] {

    def changed(b: ObservableValue[_ <: String], oldVal: String, newVal: String) = {}

  }

  test("Verify that listener still works") {
    val prop = spy(new RollbackableStringProperty("hi"))
    val noOp = mock[NoOpListener]

    prop addListener noOp
    prop set "there"

    verify(noOp, times(1)).changed(anyObject(), Matchers.eq("hi"), Matchers.eq("there"))
    verify(prop, times(1)).fireValueChangedEvent
  }

  test("Listener should be fired for rollbacks") {
    val prop = Mockito.spy(new RollbackableStringProperty("hi"))
    val noOp = mock[NoOpListener]

    prop addListener noOp
    prop set "there"
    prop rollback

    verify(noOp, times(1)).changed(anyObject(), Matchers.eq("hi"), Matchers.eq("there"))
    verify(noOp, times(1)).changed(anyObject(), Matchers.eq("there"), Matchers.eq("hi"))
    verify(prop, times(2)).fireValueChangedEvent
  }

  test("Listener wont be fired for commits") {
    val prop = spy(new RollbackableStringProperty("hi"))
    val noOp = mock[NoOpListener]

    prop addListener noOp
    prop set "there"
    prop commit

    verify(noOp, times(1)).changed(anyObject(), anyString, anyString)
    verify(prop, times(1)).fireValueChangedEvent
  }

  test("Listener fired for more than one update") {
    val prop = spy(new RollbackableStringProperty("hi"))
    val noOp = mock[NoOpListener]

    prop addListener noOp
    prop set "there"
    prop set "dude"
    prop commit

    verify(noOp, times(2)).changed(anyObject(), anyString, anyString)
    verify(prop, times(2)).fireValueChangedEvent
  }

  test("Restoring to null oldValue") {
    val prop = Mockito.spy(new RollbackableStringProperty(null))
    val noOp = mock[NoOpListener]
    prop addListener noOp

    assert(prop.get === null)

    prop set "hi"
    prop rollback

    assert(prop.get === null)

    verify(noOp, times(1)).changed(anyObject(), Matchers.eq(null), Matchers.eq("hi"))
    verify(noOp, times(1)).changed(anyObject(), Matchers.eq("hi"), Matchers.eq(null))
    verify(prop, times(2)).fireValueChangedEvent
  }

  test("Restoring to null oldValue (overloading constructor)") {
    val prop = Mockito.spy(new RollbackableStringProperty)
    val noOp = mock[NoOpListener]
    prop addListener noOp

    assert(prop.get === null)

    prop set "hi"
    prop rollback

    assert(prop.get === null)

    verify(noOp, times(1)).changed(anyObject(), Matchers.eq(null), Matchers.eq("hi"))
    verify(noOp, times(1)).changed(anyObject(), Matchers.eq("hi"), Matchers.eq(null))
    verify(prop, times(2)).fireValueChangedEvent
  }

  test("Restoring from null oldValue") {
    val prop = Mockito.spy(new RollbackableStringProperty("hi"))
    val noOp = mock[NoOpListener]
    prop addListener noOp

    prop set null
    prop rollback

    assert(prop.get === "hi")

    verify(noOp, times(1)).changed(anyObject(), Matchers.eq("hi"), Matchers.eq(null))
    verify(noOp, times(1)).changed(anyObject(), Matchers.eq(null), Matchers.eq("hi"))
    verify(prop, times(2)).fireValueChangedEvent
  }

  test("Restoring tp original oldValue") {
    val prop = new RollbackableStringProperty("hi")

    prop set "there"
    prop set "dude"
    prop rollback

    assert(prop.get === "hi")
  }
}