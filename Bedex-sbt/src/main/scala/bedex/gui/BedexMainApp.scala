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

object BedexMainApp extends JFXApp {

  stage = new PrimaryStage() {
    title = "Bedex Main"
    scene = new Scene {
      content = loadResource("Main")
    }
  }

}
//
//  private def tab(title: String, contet: Node): Tab = {
//    val sp = new ScrollPane
//    sp.fitToHeight = true
//    sp.fitToWidth = true
//    sp.content = contet
//    
//    val t = new Tab
//    t.text = title
//    t.content = sp
//    t.closable = false
//    t
//  }
//
//  private lazy val tabPane = {
//    val tp = new TabPane
//    tp += tab("Hi there", new Button("Hi there"))
//    tp += tab("HElloWW", new Button("HElloWW there"))
//    tp += tab("Labéuuu", longggg)
//    tp.minHeight = 500
//    tp.minWidth = 500
//    tp
//  }
//
//}