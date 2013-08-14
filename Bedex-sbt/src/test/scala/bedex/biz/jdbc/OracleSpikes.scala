package bedex.biz.jdbc

import java.sql.DriverManager
import java.sql.ResultSet
import scala.Array.canBuildFrom

object OracleSpikes extends App {

  import java.sql.{ Connection, DriverManager, ResultSet }

  // Load the driver
  Class forName "oracle.jdbc.driver.OracleDriver"

  // Setup the connection
  val url = "jdbc:oracle:thin:@//10.42.10.53:1521/jira"
  val conn = DriverManager.getConnection(url, "mad", "max")

  try {
    // Configure to be Read Only
    val statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)

    // Execute Query
    val rs = statement.executeQuery("SELECT user_name, team, end_date from USER_TEAM")

    // Iterate Over ResultSet
    while (rs.next) {
      println(rs.getString("user_name"))
    }

    // Execute Query2
    val rs2 = conn.createStatement.executeQuery("SELECT name, coach, manager FROM TEAM")
    val tups = for (tup <- new RsIterator(rs2)) yield (rs2 getString (1), rs2 getString (2), rs2 getString (3))

    println("---- tuples from TEAM ---")
    println(tups.mkString("\n"))

    val rs3 = conn.createStatement.executeQuery("SELECT name, coach, manager FROM TEAM ORDER BY 1")
    val cols = rs3.getMetaData.getColumnCount
    val tups2 =
      for (row <- new RsIterator(rs3)) yield for (col <- 1 to cols)  yield row.getString(col)

    println("---- tuples2 from TEAM ---")
    println(tups2.mkString("\n"))
    
    val m = Array ( Array(1,2,3), Array(10, 20, 30), Array(100, 200, 300))
    
    val tt = for(line <- m) yield for(i <- 0 until line.length) yield line(i)
    println("---- matrix ---")
    println(tt.mkString("\n"))

  } finally {
    conn.close
  }

}

class RsIterator(rs: ResultSet) extends Iterator[ResultSet] {
  def hasNext: Boolean = rs.next()
  def next(): ResultSet = rs
}