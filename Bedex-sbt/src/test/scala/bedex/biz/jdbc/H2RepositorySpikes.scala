package bedex.biz.jdbc

import java.util.Date
import bedex.biz.env.Environment1

object H2RepositorySpikes extends App {

  import H2Repository._

  //  println("Teams: \n\t" + allTeams.mkString("\n\t"))
  //  println("Users: \n" + allUsers.mkString("\n\t"))
  //  println("Logs: \n" + allMissAppointments.mkString("\n\t"))

  H2Repository.connectTo("jdbc:h2:~/test_env")

  //  val miss0 = allMissAppointments(0)
  //  println("Miss0:" + miss0)
  //  miss0.reason.set("New reason int " + new Date)
  //
  //  update(miss0)
  //
  //  println("Miss0 reloaded:" + allMissAppointments(0))

  val user = allUsers.find(_ == Environment1.worklogList.head.user).get
  val logs = lastWorklogFrom(user)

  println(s"Worklogs for ${user} are: \n ${logs}")

  shutdown

}