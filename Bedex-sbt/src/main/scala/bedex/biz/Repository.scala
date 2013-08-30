package bedex.biz

import bedex.biz.env.Environment1
import org.slf4j.LoggerFactory
import Domain._

trait Repository {
  
  lazy val logger = LoggerFactory.getLogger(getClass)

  def allTeams: List[Team]

  def allUsers: List[User]
  def allUsersFrom(t: Team): List[User] = allUsers filter (_.team == t)

  def allMissAppointments: List[MissAppointment]
  
  import scala.language.postfixOps
  
  def allMissAppointments(t: Team): List[MissAppointment] = allMissAppointments filter (_.user.team == t) sorted
  def allMissAppointments(u: User): List[MissAppointment] = allMissAppointments filter (_.user == u) sorted

  def update(miss: MissAppointment) = {
    /* no-op */
    logger.debug("Updating MissAppointment {}", miss)
  }
  
  def lastWorklogFrom(user: User): List[Worklog]
  
  def shuwtdown = ()

}

object InMemory extends Repository {

  var environment = Environment1

  def allTeams = environment.teamList

  def allUsers = environment.userList

  def allMissAppointments = environment.missAppointmentList

  def lastWorklogFrom(user: User) = environment.worklogList.filter(_.user == user)
  
}
