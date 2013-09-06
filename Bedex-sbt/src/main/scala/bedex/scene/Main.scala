package bedex.scene

import java.io.IOException
import javafx.{ fxml => jfxf }
import javafx.{ scene => jfxs }
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import java.util.ResourceBundle

object Main extends JFXApp {

  var resource = getClass.getResource("Sample.fxml")

  if (resource == null)
    throw new IOException("Cannot load resource: Sample.fxml")

  // NOTE: ScalaFX doe not yet provide a wrapper fro FXMLLoader (2012.11.12)
  // We load here FXML content using JavaFX directly.
  // It is important to provide type for the element loaded,
  // though it can be a generic, here use `javafx.scene.parent`.
  val root: jfxs.Parent = jfxf.FXMLLoader.load(resource, ResourceBundle.getBundle("Messages"))

  stage = new PrimaryStage() {
    title = "FXML Sample Demo"
    scene = new Scene(root)
  }

}