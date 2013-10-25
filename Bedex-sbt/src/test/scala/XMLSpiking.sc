

import scala.xml._

object XMLSpiking {
  val xml = <notifications>
              <notification>
                <user>john</user><reason>sick leave</reason>
              </notification>
              <notification>
                <user>doe</user><reason>velociraptor attack</reason>
              </notification>
              <notification>
                <user>john</user><reason>terminator chasing</reason>
              </notification>
            </notifications>                      //> xml  : scala.xml.Elem = <notifications>
                                                  //|               <notification>
                                                  //|                 <user>john</user><reason>sick leave</reason>
                                                  //|               </notification>
                                                  //|               <notification>
                                                  //|                 <user>doe</user><reason>velociraptor attack</reason>
                                                  //|               </notification>
                                                  //|               <notification>
                                                  //|                 <user>john</user><reason>terminator chasing</reason>
                                                  //|               </notification>
                                                  //|             </notifications>

  //(xml \ "notification")

  //(xml \ "notification" \ "user").filter(_.text == "john")
                                                  
	//(xml \\ "user").filter(_.text == "john").head.head.head
	
	//val f = (node: Node) => node.text == "john"
	(xml \ "notification").foreach( println ) //> <notification>
                                                  //|                 <user>john</user><reason>sick leave</reason>
                                                  //|               </notification>
                                                  //| <notification>
                                                  //|                 <user>doe</user><reason>velociraptor attack</reason>
                                                  //|               </notification>
                                                  //| <notification>
                                                  //|                 <user>john</user><reason>terminator chasing</reason>
                                                  //|               </notification>

	
	
	val js = (xml \ "notification").filter { n =>
		(n \ "user").text == "john"
	}                                         //> js  : scala.xml.NodeSeq = NodeSeq(<notification>
                                                  //|                 <user>john</user><reason>sick leave</reason>
                                                  //|               </notification>, <notification>
                                                  //|                 <user>john</user><reason>terminator chasing</reason>
                                                  //|               </notification>)
 
 val node = <notification>
                 <user>connor</user>
                 <reason>ttrex riding</reason>
               </notification>                    //> node  : scala.xml.Elem = <notification>
                                                  //|                  <user>connor</user>
                                                  //|                  <reason>ttrex riding</reason>
                                                  //|                </notification>
            
  // --
  val x = xml.child ++ node                       //> x  : Seq[scala.xml.Node] = ArrayBuffer(
                                                  //|               , <notification>
                                                  //|                 <user>john</user><reason>sick leave</reason>
                                                  //|               </notification>, 
                                                  //|               , <notification>
                                                  //|                 <user>doe</user><reason>velociraptor attack</reason>
                                                  //|               </notification>, 
                                                  //|               , <notification>
                                                  //|                 <user>john</user><reason>terminator chasing</reason>
                                                  //|               </notification>, 
                                                  //|             , <notification>
                                                  //|                  <user>connor</user>
                                                  //|                  <reason>ttrex riding</reason>
                                                  //|                </notification>)
  
  
  val z = <notifications>{x}</notifications>      //> z  : scala.xml.Elem = <notifications>
                                                  //|               <notification>
                                                  //|                 <user>john</user><reason>sick leave</reason>
                                                  //|               </notification>
                                                  //|               <notification>
                                                  //|                 <user>doe</user><reason>velociraptor attack</reason>
                                                  //|               </notification>
                                                  //|               <notification>
                                                  //|                 <user>john</user><reason>terminator chasing</reason>
                                                  //|               </notification>
                                                  //|             <notification>
                                                  //|                  <user>connor</user>
                                                  //|                  <reason>ttrex riding</reason>
                                                  //|                </notification></notifications>
 
 import scala.util._
 import java.io._
 
  println(Properties.userHome)                    //> /home/eduardo.vasques
  println(File.separatorChar)                     //> /
}