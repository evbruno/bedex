package bedex.biz

object DomainSpikes extends App {

  println(Users.all mkString "\n")
  println(Teams.all mkString "\n")

}