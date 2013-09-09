package bedex.gui

import java.net.URL
import java.util
import java.util.List

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField

// only with JavaFX
class HolidayController extends Initializable {

  @FXML
  private var saveButton: Button = _

  @FXML
  private var cancelButton: Button = _

  @FXML
  private var descriptionText: TextField = _

  @FXML
  private var dateText: TextField = _

  @FXML
  private var holidaysTable: TableView[Bean] = _

  @FXML
  private var dateColumn: TableColumn[Bean, String] = _

  @FXML
  private var descriptionColumn: TableColumn[Bean, String] = _

  private var count = 0

  private val source = FXCollections.observableArrayList(randomStuff)

  @FXML
  private def onSaveAction(event: ActionEvent) {
    count = count + 1
    println(s"Count = ${count}")
    source.add(0, Bean(count + 50))
  }

  @FXML
  private def onCancelAction(event: ActionEvent) {
    source.remove(count)
    count = 0
    println(s"Count = ${count}")
  }

  def initialize(url: URL, rb: util.ResourceBundle) {
    holidaysTable.setItems(source)
    println(s"Table items = ${source}")
  }

  private def randomStuff: List[Bean] = collection.JavaConversions.seqAsJavaList(Range(1, 50).map(Bean))

}

case class Bean(n: Int) {

  private val numProp = new SimpleStringProperty(n.toString * 3)
  private val charProp = new SimpleStringProperty((n + 64).toChar.toString)

  def dateProperty = numProp
  def descriptionProperty = charProp

}