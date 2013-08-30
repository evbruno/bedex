package bedex.view

import scalafx.scene.control.cell.TextFieldTableCell
import bedex.biz.MissAppointment
import javafx.scene.control.Tooltip
import javafx.scene.control.{ cell => jfxCell }
import javafx.util.converter.DefaultStringConverter

class MissAppointmentCell extends TextFieldTableCell(new DirtyTextFieldTableCell)

private class DirtyTextFieldTableCell extends jfxCell.TextFieldTableCell[MissAppointment, String](new DefaultStringConverter) {

  override def updateItem(item: String, empty: Boolean) = {
    val miss = getTableRow.getItem.asInstanceOf[MissAppointment]
    if (miss != null) updateStyle(miss)

    super.updateItem(item, empty)
  }

  private def reasonFor(miss: MissAppointment) =
    if (miss.reason.get.isEmpty) "<No reason>"
    else miss.reason.get

  def updateStyle(miss: MissAppointment) {
    setTooltip(new Tooltip(reasonFor(miss)))

    if (!miss.isDirty) {
      if (getTableRow.getStyleClass.contains("dirtyCell")) {
        getTableRow.getStyleClass.remove("dirtyCell")
        getTableRow.setVisible(false)
        getTableRow.setVisible(true)
      }
    } else {
      if (!getTableRow.getStyleClass.contains("dirtyCell")) {
        getTableRow.getStyleClass.add("dirtyCell")
        getTableRow.setVisible(false)
        getTableRow.setVisible(true)
      }
    }

  }
}

class StylishTableCell[T](val newStyles: String*) extends TextFieldTableCell(new JFXStylishTableCell[T](newStyles:_*))

class JFXStylishTableCell[T](val newStyles: String*) extends jfxCell.TextFieldTableCell[T, String] {

  override def updateItem(item: String, empty: Boolean) = {
    getStyleClass.addAll(newStyles: _*)
    super.updateItem(item, empty)
  }

}
