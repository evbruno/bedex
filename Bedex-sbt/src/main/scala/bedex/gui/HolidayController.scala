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

// only with JavaFX
class HolidayController extends Initializable with Logger {

  @FXML
  private var saveButton: Button = _

  @FXML
  private var cancelButton: Button = _

  @FXML
  private var deleteButton: Button = _

  @FXML
  private var descriptionText: TextField = _

  @FXML
  private var dateText: TextField = _

  @FXML
  private var holidaysTable: TableView[HolidayProperties] = _

  @FXML
  private var dateColumn: TableColumn[HolidayProperties, String] = _

  @FXML
  private var descriptionColumn: TableColumn[Holiday, String] = _

  private lazy val source: ObservableList[HolidayProperties] = FXCollections.observableArrayList()

  private var displayDateFormat: SimpleDateFormat = _
  private var inputDateFormat: SimpleDateFormat = _

  def initialize(url: URL, rb: util.ResourceBundle) {
    reloadSource()
    holidaysTable.setItems(source)

    inputDateFormat = new SimpleDateFormat(rb.getString("core.date.shortFormat"))
    displayDateFormat = new SimpleDateFormat(rb.getString("core.date.longFormat"))
    initBindings
  }

  private def reloadSource() {
    source.clear()
    source.addAll(FXCollections.observableArrayList(HolidayProperties.toList: _*))
  }

  @FXML
  private def onSaveAction(event: ActionEvent) {
    println(s"Saving ")

    val name = descriptionText.getText
    val date = inputDateFormat.parse(dateText.getText)
    val holiday = Holiday(name, date)

    debug("Saving holiday = {} from name = {} and date = {}", holiday, name, date)
    Holiday.insert(holiday)

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
    val property = holidaysTable.getSelectionModel.getSelectedItem

    debug("Deleting property = {}", property)
    Holiday.delete(property.source)

    reloadSource()
    clearForm()
  }

  @FXML
  private def onMouseClicked(event: MouseEvent) {
    debug("Clicked on = {}", holidaysTable.getSelectionModel.getSelectedItem)
  }

  private def clearForm(): Unit = {
    holidaysTable.getSelectionModel.clearSelection()
    descriptionText.setText("")
    dateText.setText("")
  }

  case class HolidayProperties(val source: Holiday) {

    def descriptionProperty = new SimpleStringProperty(source.name)
    def dateProperty = new SimpleStringProperty(displayDateFormat.format(source.when))

  }

  object HolidayProperties {
    def toList = Holiday.all.map(HolidayProperties(_))
  }
  
  private def initBindings: Unit = {

    deleteButton.disableProperty.bind(holidaysTable.getSelectionModel.selectedItemProperty.isNull)

    saveButton.disableProperty.bind(
      descriptionText.textProperty.isEqualTo("").or(dateText.textProperty.isEqualTo("")))

    cancelButton.disableProperty.bind(deleteButton.disabledProperty
      .and(
        descriptionText.textProperty.isEqualTo("").and(dateText.textProperty.isEqualTo(""))))
  }

}