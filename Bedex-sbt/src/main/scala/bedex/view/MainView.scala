package bedex.view

import org.slf4j.LoggerFactory

import bedex.biz.MissAppointment
import javafx.scene.control.Dialogs
import scalafx.Includes.eventClosureWrapper
import scalafx.Includes.jfxStringProperty2sfx
import scalafx.Includes.jfxTableViewSelectionModel2sfx
import scalafx.Includes.tableColumnCellEditEventClosureWrapper
import scalafx.beans.property.BooleanProperty
import scalafx.beans.property.BooleanProperty.sfxBooleanProperty2jfx
import scalafx.beans.property.ReadOnlyStringWrapper
import scalafx.collections.ObservableBuffer
import scalafx.geometry.HPos
import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.scene.Node
import scalafx.scene.control.Button
import scalafx.scene.control.ComboBox
import scalafx.scene.control.TableColumn
import scalafx.scene.control.TableColumn.CellEditEvent
import scalafx.scene.control.TableColumn.sfxTableColumn2jfx
import scalafx.scene.control.TableView
import scalafx.scene.control.TableView.sfxTableView2jfx
import scalafx.scene.control.Tooltip.stringToTooltip
import scalafx.scene.layout.GridPane
import scalafx.scene.text.Text
import scalafx.stage.Stage.sfxStage2jfx

//FIXME
// + provide some root Node (extension or wrapping)
// + ...finish him !!!
class MainView {

  private val COMBO_WIDTH = 150
  private val BUTTON_WIDTH = 100
  private lazy val logger = LoggerFactory.getLogger(getClass)

  import javafx.{ event => evt }
  import javafx.scene.{ input => ipt }
  import javafx.beans.{ value => jval }

  val controller = new Controller

  val teamsCombo = new ComboBox(controller.teams) {
    prefWidth = COMBO_WIDTH
    converter = new DisplayableObjectReadonlyConverter
    value onChange {
      usersCombo.items = controller.usersFrom(value.value)
      logger.debug("ComboBox changed: {}", value.value)
    }
  }

  val usersCombo = new ComboBox(controller.users) {
    prefWidth = COMBO_WIDTH
    converter = new DisplayableObjectReadonlyConverter
    selectionModel.value.selectFirst()
  }

  val filterCombo = new ComboBox(controller.filterOptions) {
    prefWidth = COMBO_WIDTH
    selectionModel.value.selectFirst()
  }

  val levelCombo = new ComboBox[Option[Int]] {
    items = ObservableBuffer(Seq(None, Some(0), Some(1), Some(2), Some(3)))
    converter = new SomeConverter("Lvl: ")
    selectionModel.value.selectFirst()
  }

  def onSearchAction = {
    val team = teamsCombo.value.value
    val user = usersCombo.value.value
    val filter = filterCombo.value.value
    val level = levelCombo.value.value

    logger.debug("Searchin for team = {}, user = {}, filter = {}, level = {}", team, user, filter, level)

    table.items = controller.missApointments(team, user, level)
  }

  val searchButton = new Button {
    text = "Search"
    prefWidth = BUTTON_WIDTH
    defaultButton = true
    tooltip = "Search button: \n click here to search."
    onAction = onSearchAction
  }

  def onSaveAction = {
    controller.save
    table.getItems.clear
  }

  val saveButton = new Button {
    text = "Save"
    prefWidth = 100
    onAction = onSaveAction
    disable <== controller.isEmpty
  }

  def onCancelAction = {
    val response = Dialogs showConfirmDialog (null,
      "Do you want to cancel your modifications ?",
      "Cancel alterations",
      "Canceling",
      Dialogs.DialogOptions.YES_NO)

    logger.debug("Confirmation response: {}", response)

    if (response == Dialogs.DialogResponse.YES) {
      controller.cancel
      table.getItems.clear
    }
  }

  val cancelButton = new Button("Cancel") {
    prefWidth = 100
    onAction = onCancelAction
    disable <== saveButton.disable
  }

  def onWorklogAction = {
    worklogDialogStage.show
    updateWorklog()
  }

  val worklogButton = new Button("See worklog") {
    onAction = onWorklogAction
    disable <== tableRowHasSelection.not
  }

  val userCol = new TableColumn[MissAppointment, String] {
    text = "User"
    prefWidth = 150
    resizable = true
    sortable = true
    cellValueFactory = { e: MyColumnFeature => ReadOnlyStringWrapper(e.getValue.user.name) }
  }

  val teamCol = new TableColumn[MissAppointment, String] {
    text = "Team"
    resizable = false
    prefWidth = 80
    cellValueFactory = { e: MyColumnFeature => ReadOnlyStringWrapper(e.getValue.user.team.name) }
  }

  val startDateCol = new TableColumn[MissAppointment, String] {
    text = "Start Dt"
    prefWidth = 70
    resizable = false
    sortable = true
    cellValueFactory = (e: MyColumnFeature) => ReadOnlyStringWrapper(e.getValue.startDate)
  }

  val endDateCol = new TableColumn[MissAppointment, String] {
    text = "End Dt"
    prefWidth = 70
    resizable = false
    sortable = true
    cellValueFactory = (e: MyColumnFeature) => ReadOnlyStringWrapper(e.getValue.endDate)
  }

  val logTypeCol = new TableColumn[MissAppointment, String] {
    text = "Type"
    prefWidth = 50
    resizable = false
    cellValueFactory = (e: MyColumnFeature) => ReadOnlyStringWrapper(e.getValue.typeLog.toString.drop(1))
  }

  val levelCol = new TableColumn[MissAppointment, String] {
    text = "Lvl"
    cellFactory = _ => new StylishTableCell("centerCell")
    prefWidth = 40
    resizable = false
    sortable = true
    cellValueFactory = (e: MyColumnFeature) => ReadOnlyStringWrapper(e.getValue.levelLog.toString)
  }

  val workedCol = new TableColumn[MissAppointment, String] {
    text = "Worked (h)"
    prefWidth = 60
    resizable = false
    cellValueFactory = (e: MyColumnFeature) => ReadOnlyStringWrapper(e.getValue.worked)
  }

  val reasonCol = new TableColumn[MissAppointment, String] {
    text = "Reason"
    prefWidth = 250
    editable = true
    resizable = true
    cellValueFactory = _.value.reason
    cellFactory = _ => new MissAppointmentCell
    onEditCommit = (evt: CellEditEvent[MissAppointment, String]) => {
      evt.rowValue.reason.value = evt.newValue
      controller.putDirty(evt.rowValue)
    }

  }

  val justLabel = new Text("Work log tracking")

  val table = new TableView[MissAppointment] {
    editable = true
    prefWidth = 720
    prefHeight = 400
    columns ++= List(userCol, teamCol, startDateCol, endDateCol, logTypeCol, levelCol, workedCol, reasonCol)
    selectionModel.value.selectedItem.onChange {
      updateWorklog()
    }
  }

  private lazy val tableRowHasSelection = new BooleanProperty { value = false }

  def updateWorklog() {
    val currentItem = table.selectionModel.value.getSelectedItem
    tableRowHasSelection.set(currentItem != null)

    if (worklogDialogStage.isShowing) {
      val user = if (currentItem != null) Some(currentItem.user) else None
      controller.lastWorklogsFrom(user)
    }
  }

  private lazy val worklogDialogStage = new WorklogStage(controller.worklogsBuffer)

  def root: Node = {
    val grid = new GridPane {
      alignment = Pos.TOP_LEFT
      vgap = 10
      hgap = 10
      padding = Insets(25, 25, 25, 25)
      //gridLinesVisible = true
    }

    grid.add(justLabel, 0, 0, 5, 1)

    grid.add(teamsCombo, 0, 1)
    grid.add(usersCombo, 1, 1)
    grid.add(levelCombo, 2, 1)
    grid.add(searchButton, 4, 1)

    grid.add(table, 0, 2, 5, 20)

    grid.add(saveButton, 0, 22, 2, 1)
    GridPane setHalignment (saveButton, HPos.CENTER)
    grid.add(cancelButton, 3, 22)
    grid.add(worklogButton, 4, 22)

    grid
  }

}