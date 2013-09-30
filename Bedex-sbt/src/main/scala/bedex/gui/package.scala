package bedex

import scalafx.scene.Node
import scalafx.scene.Group
import java.util.ResourceBundle

package object gui {
  
  val resourceBundle = ResourceBundle.getBundle("Messages")
  
  import javafx.{ fxml => jfxf }
  import javafx.{ scene => jfxs }

  def loadResource(name: String) : Node = {
    val fxml = getClass.getResource(name + ".fxml")

    if (fxml == null)
      throw new RuntimeException(s"Cannot load resource: ${name}.fxml !!")

    val root: jfxs.Parent = jfxf.FXMLLoader.load(fxml, resourceBundle)
    
    new Group(root)
  }

}