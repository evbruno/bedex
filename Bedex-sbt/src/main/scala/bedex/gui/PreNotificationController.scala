package bedex.gui

import javafx.scene.control.TabPane
import javafx.fxml.FXML
import javafx.fxml.Initializable
import java.net.URL
import javafx.scene.control.ComboBox
import javafx.collections.ObservableList
import javafx.collections.FXCollections
import bedex.view.SomeConverter
import javafx.beans.property.ReadOnlyStringProperty
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.scene.control.TableView

// FIXME impl. postponed !
class PreNotificationController extends Initializable {

  @FXML private var levelComboBox: ComboBox[Option[Int]] = _

  @FXML private var preNotificationTableView: TableView[PreNotification] = _

  def initialize(url: URL, rb: java.util.ResourceBundle) {
    levelComboBox.setItems(FXCollections.observableArrayList(None, Some(0), Some(1), Some(2)))
    levelComboBox.setConverter(new SomeConverter[Int]("Lvl: "))
  }

}

case class PreNotification(val user: String, val date: String, val level: Option[Int], val reason: String) {

  val userProperty = new ReadOnlyStringWrapper(user)
  val dateProperty = new ReadOnlyStringWrapper(date)
  val levelProperty = new ReadOnlyStringWrapper(level.toString)
  val reasonProperty = new ReadOnlyStringWrapper(reason)

}