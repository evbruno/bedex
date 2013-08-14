package bedex.biz

object InMemorySpikes extends App {

  val t0 = InMemory.allTeams(0)
  println("team at 0 : " + t0)

  println(InMemory.allUsersFrom(t0).mkString("\n"))

  val u0 = InMemory.allUsers(0)
  println("user at 0 : " + u0)

  println(InMemory.allMissAppointments(0))
  println(InMemory.allMissAppointments(t0).mkString("\n"))
  println(InMemory.allMissAppointments(u0).mkString("\n"))

}