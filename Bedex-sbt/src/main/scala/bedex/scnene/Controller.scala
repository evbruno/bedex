package bedex.scene

import java.net.URL
import java.util
import javafx.scene.{ layout => jfxsl }
import javafx.{ event => jfxe }
import javafx.{ fxml => jfxf }
import javafx.scene.{ control => ctrl }
import scalafx.scene.layout.AnchorPane
import scalafx.scene.control.Button
import scalafx.scene.control.Label

class Controller extends jfxf.Initializable {

  @jfxf.FXML
  private var myButton: ctrl.Button = _
  private var button: Button = _

  @jfxf.FXML
  private var myLabel: ctrl.Label = _
  private var label: Label = _

  @jfxf.FXML
  private var myLabelCount: ctrl.Label = _
  private var labelCount: Label = _

  private var count = 0

  @jfxf.FXML
  private def onMouseAction(event: jfxe.ActionEvent) {
    count = count + 1
    val msg = s"Hi there: ${new java.util.Date}"

    println(msg)
    label.text = msg
    labelCount.text = s"${count} time${if (count > 1) "s" else ""} clicked!"
  }

  def initialize(url: URL, rb: util.ResourceBundle) {
    button = new Button(myButton)
    label = new Label(myLabel)
    labelCount = new Label(myLabelCount)
  }
}