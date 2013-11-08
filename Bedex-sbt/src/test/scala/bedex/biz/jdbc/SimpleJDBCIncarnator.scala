package bedex.biz.jdbc

import java.sql.DriverManager

object SimpleJDBCIncarnator extends App {
  
  Class forName "org.h2.Driver"
  val url = "jdbc:h2:~/test_env"

  val conn = DriverManager.getConnection(url)
  conn.setAutoCommit(false)
  
  val rs = conn.createStatement.executeQuery("select user_name, team, end_date from user_team")
  
  while (rs.hasNext) println(rs.getString(1))
  
  conn.close()

}