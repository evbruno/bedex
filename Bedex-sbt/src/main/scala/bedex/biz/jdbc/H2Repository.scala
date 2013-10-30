package bedex.biz.jdbc

import java.sql._
import bedex.biz._

class H2Repository(
  val url: String,
  private val user: String = null,
  private val passwd: String = null) extends Repository with JDBCCommonRepo {

  Class.forName("org.h2.Driver")
  
  implicit val connection = DriverManager.getConnection(url, user, passwd)
  connection.setAutoCommit(false)

  override def shutdown() = {
    super.shutdown
    logger.info("H2 is desconecting from {}", url)
  }

}
