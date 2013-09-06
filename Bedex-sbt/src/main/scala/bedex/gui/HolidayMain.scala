package bedex.gui

import java.io.IOException
import javafx.{ fxml => jfxf }
import javafx.{ scene => jfxs }
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import java.util.ResourceBundle

object HolidayMain extends JFXApp {

  var resource = getClass.getResource("Holiday.fxml")

  if (resource == null)
    throw new RuntimeException("Cannot load resource: Holiday.fxml")

  val root: jfxs.Parent = jfxf.FXMLLoader.load(resource, ResourceBundle.getBundle("Messages"))

  stage = new PrimaryStage() {
    title = "Holiday"
    scene = new Scene(root)
  }

}