package bedex.gui

import javafx.fxml._
import javafx.scene.control._
import java.net.URL
import javafx.scene.layout._
import javafx.scene.Node
import bedex.view.MainView

class MainController extends Initializable {

  @FXML
  private var mainContentTabPane: TabPane = _

  @FXML
  private var holidayTab: Tab = _

  @FXML
  private var vacationTab: Tab = _

  @FXML
  private var missAppointmentTab: Tab = _

  @FXML
  private var preNotificationTab: Tab = _

  def initialize(url: URL, rb: java.util.ResourceBundle) {
    missAppointmentTab.setContent(new MainView().root)
    holidayTab.setContent(tabContent("Holiday"))
    vacationTab.setContent(tabContent("Vacation"))
    //preNotificationTab.setContent(tabContent("PreNotification"))
  }

  private def tabContent(resourceName: String): Node = {
    val sp = new scalafx.scene.control.ScrollPane
    sp.fitToHeight = true
    sp.fitToWidth = true
    sp.content = loadResource(resourceName)
    sp
  }

}