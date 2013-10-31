package bedex.gui

import java.net.URL
import java.text.SimpleDateFormat
import java.util
import bedex.biz.Holiday
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import bedex.core.Logger
import javafx.scene.control.ComboBox
import bedex.biz.User
import bedex.biz.Users
import bedex.view.AnyConverter
import bedex.biz.Vacation

class VacationController extends Initializable with Logger {

  @FXML
  private var saveButton: Button = _

  @FXML
  private var cancelButton: Button = _

  @FXML
  private var deleteButton: Button = _

  @FXML
  private var reasonText: TextField = _

  @FXML
  private var startDateText: TextField = _

  @FXML
  private var endDateText: TextField = _

  @FXML
  private var vacationsTable: TableView[VacationProperties] = _

  @FXML
  private var userCombo: ComboBox[User] = _

  private lazy val source: ObservableList[VacationProperties] = FXCollections.observableArrayList()

  private var displayDateFormat: SimpleDateFormat = _
  private var inputDateFormat: SimpleDateFormat = _

  def initialize(url: URL, rb: util.ResourceBundle) {
    reloadSource()
    vacationsTable.setItems(source)

    userCombo.setItems(FXCollections.observableArrayList(Users.all: _*))
    userCombo.setConverter(new AnyConverter[User](_.name))

    inputDateFormat = new SimpleDateFormat(rb.getString("core.date.shortFormat"))
    displayDateFormat = new SimpleDateFormat(rb.getString("core.date.longFormat"))
    initBindings
  }

  private def reloadSource() {
    source.clear()
    source.addAll(FXCollections.observableArrayList(VacationProperties.toList: _*))
  }

  @FXML
  private def onSaveAction(event: ActionEvent) {
    println(s"Saving ")

    val user = userCombo.getSelectionModel.getSelectedItem
    val reason = reasonText.getText
    val startDate = inputDateFormat.parse(startDateText.getText)
    val endDate = inputDateFormat.parse(endDateText.getText)
    val vacation = Vacation(user, reason, startDate, endDate)

    debug("Saving vacation = {} ", vacation)
    Vacation.insert(vacation)

    reloadSource()
    clearForm()
  }

  @FXML
  private def onCancelAction(event: ActionEvent) {
    debug("Canceling form")
    clearForm()
  }

  @FXML
  private def onDeleteAction(event: ActionEvent) {
    val property = vacationsTable.getSelectionModel.getSelectedItem

    debug("Deleting property = {}", property)
    Vacation.delete(property.source)

    reloadSource()
    clearForm()
  }

  @FXML
  private def onMouseClicked(event: MouseEvent) {
    debug("Clicked on = {}", vacationsTable.getSelectionModel.getSelectedItem)
  }

  private def clearForm() {
    vacationsTable.getSelectionModel.clearSelection()
    userCombo.getSelectionModel().clearSelection()

    reasonText.setText("")
    startDateText.setText("")
    endDateText.setText("")
  }

  case class VacationProperties(val source: Vacation) {

    def userNameProperty = new SimpleStringProperty(source.user.name)
    def startDateProperty = new SimpleStringProperty(displayDateFormat.format(source.startDate))
    def endDateProperty = new SimpleStringProperty(displayDateFormat.format(source.endDate))
    def reasonProperty = new SimpleStringProperty(source.reason)

  }

  object VacationProperties {

    def toList: List[VacationProperties] = Vacation.all.map(VacationProperties(_))

  }
  
  private def initBindings {

    deleteButton.disableProperty.bind(vacationsTable.getSelectionModel.selectedItemProperty.isNull)

    saveButton.disableProperty.bind(
      reasonText.textProperty.isEqualTo("")
        .or(startDateText.textProperty.isEqualTo(""))
        .or(endDateText.textProperty.isEqualTo(""))
        .or(userCombo.getSelectionModel.selectedItemProperty.isNull))

    cancelButton.disableProperty.bind(deleteButton.disabledProperty
        .and(reasonText.textProperty.isEqualTo("")
	        .and(startDateText.textProperty.isEqualTo(""))
	        .and(endDateText.textProperty.isEqualTo(""))
	        .and(userCombo.getSelectionModel.selectedItemProperty.isNull))
        )
  }

}