package bedex.view

import scalafx.scene.control.cell.TextFieldTableCell
import bedex.biz.MissAppointment
import javafx.scene.control.Tooltip

class MissAppointmentCell extends TextFieldTableCell(new DirtyTextFieldTableCell)

private class DirtyTextFieldTableCell extends javafx.scene.control.cell.TextFieldTableCell[MissAppointment, String](new javafx.util.converter.DefaultStringConverter) {

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
