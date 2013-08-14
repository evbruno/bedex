
import scala.util.Properties

object wtv {
  val v = Properties.javaVersion + ".666"         //> v  : String = 1.7.0_21.666
  val Array(min, ver, fullUp, _) = v.split("\\.") //> min  : String = 1
                                                  //| ver  : String = 7
                                                  //| fullUp  : String = 0_21
 
  val Array(_, realUp) = fullUp.split("\\_")      //> realUp  : String = 21
  
}