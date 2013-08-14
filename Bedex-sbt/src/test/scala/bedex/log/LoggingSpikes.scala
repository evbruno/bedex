package bedex.log

import Math._
import org.slf4j.LoggerFactory

object LoggingSpikes extends App {
  
  val logger = LoggerFactory.getLogger(getClass)

  println("Hi Logger sysout")

  logger trace("Hi Logger trace {}", (PI/0).toString) // lazy (:
  logger debug ("Hi Logger debug: {}", (2/PI).toString)
  logger debug ("Hi Logger debug0: {} debug1: {} ", Seq("a", "b"):_*)
  logger info ("Hi Logger infoA: {} infoB: {} infoC: {}", "a", "b", "c")
  logger info "Hi Logger info"
  logger warn "Hi Logger warn"
  logger error "Hi Logger error"

}
