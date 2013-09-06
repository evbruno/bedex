package bedex.gui

import java.net.URL
import java.util
import javafx.{ event => jfxe }
import javafx.{ fxml => jfxf }
import javafx.{ util => jfxu }
import javafx.scene.{ control => ctrl }
import javafx.collections.ObservableList
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import java.util.List
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty

// only with JavaFX
class HolidayController extends jfxf.Initializable {

  @jfxf.FXML
  private var saveButton: ctrl.Button = _

  @jfxf.FXML
  private var cancelButton: ctrl.Button = _

  @jfxf.FXML
  private var descriptionText: ctrl.TextField = _

  @jfxf.FXML
  private var dateText: ctrl.TextField = _

  @jfxf.FXML
  private var holidaysTable: ctrl.TableView[Bean] = _

  @jfxf.FXML
  private var dateColumn: ctrl.TableColumn[Bean, String] = _

  @jfxf.FXML
  private var descriptionColumn: ctrl.TableColumn[Bean, String] = _

  private var count = 0

  private val source = FXCollections.observableArrayList(randomStuff)

  @jfxf.FXML
  private def onSaveAction(event: jfxe.ActionEvent) {
    count = count + 1
    println(s"Count = ${count}")
    source.add(0, Bean(count + 50))
  }

  @jfxf.FXML
  private def onCancelAction(event: jfxe.ActionEvent) {
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