package bedex.biz

import scala.util.Properties
import scala.xml.Elem
import org.xml.sax.SAXParseException

case class PreNotification(user: String, reason: String)

object PreNotification {

  def all: List[PreNotification] = ???

  def insert(pn: PreNotification) = ???

  def delete(pn: PreNotification) = ???

}

// XML

object XMLExporter {

  import scala.util._
  import java.io._

  private val fileName = Properties.userHome + File.separatorChar + "bedex-notifications.xml"

  def getFile: File = {
    val file = new File(fileName)
    if (!file.exists) createOrCry(file)
    file
  }

  private def createOrCry(file: java.io.File): AnyVal = {
    try {
      file.createNewFile
    } catch {
      case e: Throwable => throw new RuntimeException(s"Cannot create file ${fileName}... because ${e}")
    }
  }
  
  def load() : Option[Elem] = {
    val f = getFile
    try {
    	val root = scala.xml.XML.load(f.toString)
    	Some(root)
    } catch {
      case e: SAXParseException => generateFileIfNeeded(f); None
    }
  }
  
  private def generateFileIfNeeded(file: File) {
    
  }

}

object Main extends App {

  import XMLExporter._
  import java.io._

  println(getFile)
  println(load)
}