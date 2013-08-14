package bedex.biz.jdbc

import java.util.Date

object H2RepositorySpikes extends App {

  import H2Repository._

  //  println("Teams: \n\t" + allTeams.mkString("\n\t"))
  //  println("Users: \n" + allUsers.mkString("\n\t"))
  //  println("Logs: \n" + allMissAppointments.mkString("\n\t"))

  val miss0 = allMissAppointments(0)
  println("Miss0:" + miss0)
  miss0.reason.set("New reason int " + new Date)

  update(miss0)

  println("Miss0 reloaded:" + allMissAppointments(0))

  shutdown

}