package bedex.core

import org.slf4j.LoggerFactory

trait Logger {
  
  private lazy val logger = LoggerFactory.getLogger(super.getClass)
  
  def debug(message: String) = logger.debug(message)
  
  def debug(message: String, objects: Object*) = logger.debug(message, objects:_*)

}