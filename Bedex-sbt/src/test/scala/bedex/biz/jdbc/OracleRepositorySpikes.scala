package bedex.biz.jdbc

import java.util.Date
import bedex.biz.env.Environment1
import bedex.biz.Holiday
import bedex.biz.Vacation

object OracleRepositorySpikes extends App {

  val repo = new OracleRepository("jdbc:oracle:thin:@//10.42.10.53:1521/jira", "mad", "max")
  
  println(repo.allVacations.mkString("\n\t"))
  println("---x----")
  println(repo.allHolidays.mkString("\n\t"))
  
  repo.shutdown

}