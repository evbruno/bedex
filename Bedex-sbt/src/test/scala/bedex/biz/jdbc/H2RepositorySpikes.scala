package bedex.biz.jdbc

import java.util.Date
import bedex.biz.env.Environment1
import bedex.biz.Holiday
import bedex.biz.Vacation

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

  //  val user = allUsers.find(_ == Environment1.worklogList.head.user).get
  //  val logs = lastWorklogFrom(user)
  //
  //  println(s"Worklogs for ${user} are: \n ${logs}")

  //  println(allHolidays.mkString("\n\t"))
  //  
  //  val halloween = allHolidays.find(_.name == "Halloween").get
  //  delete(halloween)

  //  val todays = Holiday("Todays day", new java.util.Date)
  //  insert(todays)
  //  
  //  println(allHolidays.mkString("\n\t"))
  //
  //  delete(todays)
  //  println(allHolidays.mkString("\n\t"))

//  println(allVacations.mkString("\n\t"))
//  
//  val vac = allVacations.head
//  println("Deleting " + vac)
//  delete(vac)
//  
  println(allVacations.mkString("\n\t"))
  
  val u = allUsers.find(_.name == "willie.clumpsy").get
  val today = new java.util.Date
  val tomorrow = new java.util.Date(today.getTime + 24 * 60 * 60 * 1000)
  val vac = Vacation(u, "got lost", today, tomorrow)

  println 
  println("Inserting " + vac)
  insert(vac)
  println 

  println(allVacations.mkString("\n\t"))
  
  shutdown

}