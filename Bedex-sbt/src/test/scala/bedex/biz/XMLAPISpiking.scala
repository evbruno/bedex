package bedex.biz

import org.scalatest.FunSuite
import scala.xml.NodeSeq

class XMLAPISpiking extends FunSuite {

  val xml = <notifications>
              <notification>
                <user>john</user>
                <reason>sick leave</reason>
              </notification>
              <notification>
                <user>connor</user>
                <reason>velociraptor attack</reason>
              </notification>
              <notification>
                <user>john</user>
                <reason>terminator chasing</reason>
              </notification>
            </notifications>

  test("Counting notification nodes") {
    assert((xml \ "notification").size === 3)
  }

  val finder = (who: String) => (xml \ "notification") filter (n => (n \ "user").text == who)

  test("Counting nodes with value") {
    assert(finder("john").size === 2)
    assert(finder("connor").size === 1)
    assert(finder("homer").size === 0)
  }

  test("Removing some node") {
    val remover = (reason: String) => (xml \ "notification") filterNot (n => (n \ "reason").text == reason)

    assert(remover("sick leave").size === 2)
    assert(remover("any stuff").size === 3)
  }

  test("Adding a node") {
    val node = <notification>
                 <user>connor</user>
                 <reason>ttex riding</reason>
               </notification>

    val newXml = <notifications>{ xml.child ++ node }</notifications>

    assert((newXml \ "notification").size === 4)
  }

  test("Loading from file") {
    val newXml = scala.xml.XML.load(getClass.getResourceAsStream("/notifications.xml"))

    assert((newXml \ "notification").size === 3)
    assert(finder("john").size === 2)
    assert(finder("connor").size === 1)
  }

  test("Writing to file") {
    val file = getClass.getResource("/").getPath + "notifications-wr.xml"
    new java.io.File(file).createNewFile
    scala.xml.XML.save(file, xml)

    val newXml = scala.xml.XML.load(file)

    assert((newXml \ "notification").size === 3)
    assert(finder("john").size === 2)
    assert(finder("connor").size === 1)
  }

}