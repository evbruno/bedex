package bedex

import bedex.view.MainView
import scalafx.application.JFXApp
import scalafx.scene.Scene
import javafx.event.EventHandler
import javafx.stage.WindowEvent
import scalafx.application.Platform

object BedexApp extends JFXApp {

  val bootstrap = Bootstrap(parameters)

  val mainScene = new Scene(780, 550) {
    val css = getClass.getResource("/bedex.css")
    stylesheets.add(css.toExternalForm)
    content = new MainView().root
  }

  stage = new JFXApp.PrimaryStage {
    title = "Bedex"
    resizable = false
    scene = mainScene
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