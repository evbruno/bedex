package bedex.view

import bedex.biz.Worklog
import javafx.beans.property.SimpleStringProperty
import scalafx.application.JFXApp
import scalafx.beans.property.ReadOnlyStringWrapper
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.control.Label
import scalafx.scene.control.TableColumn
import scalafx.scene.control.TableColumn.CellDataFeatures
import scalafx.scene.control.TableColumn.sfxTableColumn2jfx
import scalafx.scene.control.TableView
import scalafx.stage.Stage
import scalafx.stage.Stage.sfxStage2jfx
import scalafx.stage.StageStyle
import scalafx.stage.Window.sfxWindow2jfx

class WorklogStage(data: ObservableBuffer[Worklog]) extends Stage {

  initStyle(StageStyle.UTILITY)
  initOwner(JFXApp.STAGE)
  scene = new WorklogScene(data)
  title <== worklogUserName
  resizable = false

  override def show = {
    y = owner.get.getY
    x = owner.get.getX + owner.get.width.value

    super.show
  }

  private val noWorklog = "< no worklog >"

  private lazy val worklogUserName = new SimpleStringProperty { set(noWorklog) }

  data.onChange {
    if (data.isEmpty) worklogUserName.set(noWorklog)
    else worklogUserName.set(data.head.user.name)
  }

}

private class WorklogScene(data: ObservableBuffer[Worklog]) extends Scene(142, 550) {

  val dateCol = new TableColumn[Worklog, String] {
    text = "Date"
    prefWidth = 70
    cellValueFactory = (e: CellDataFeatures[Worklog, String]) => ReadOnlyStringWrapper(e.value.date)
  }

  val workedCol = new TableColumn[Worklog, String] {
    text = "Log (h)"
    prefWidth = 70
    cellFactory = _ => new StylishTableCell("-fx-alignment: baseline-center;")
    cellValueFactory = (e: CellDataFeatures[Worklog, String]) => ReadOnlyStringWrapper(e.value.worked)
  }

  val table = new TableView[Worklog](data) {
    columns ++= List(dateCol, workedCol)
    prefHeight = 550
    maxWidth = 142
    placeholder = new Label("< empty >")
  }

  content = table

}
