package bedex.gui

import scalafx.application.JFXApp
import scalafx.scene.Parent
import scalafx.scene.Group
import scalafx.scene.control.Button
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.Tab
import scalafx.scene.control.TabPane
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.HBox
import scalafx.scene.layout.VBox
import scalafx.scene.control.ScrollPane
import java.util.ResourceBundle
import bedex.Bootstrap
import javafx.event.EventHandler
import javafx.stage.WindowEvent

object BedexMainApp extends JFXApp {

  val bootstrap = Bootstrap(parameters)

  stage = new PrimaryStage() {
    title = "Bedex Main"
    scene = new Scene {
      content = loadResource("Main")
    }
    onCloseRequest = new EventHandler[WindowEvent]() {
      override def handle(ev: WindowEvent) = {
        bootstrap.shutdown
      }
    }
  }
  
  stage.x = 75
  stage.y = 75
  stage.show

}