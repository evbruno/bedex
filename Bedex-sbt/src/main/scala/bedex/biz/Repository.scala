package bedex.biz

import org.slf4j.LoggerFactory

import Domain.MissAppointmentOrdering
import bedex.ShutdownAgent
import bedex.biz.env.Environment1

trait Repository extends ShutdownAgent {

  lazy val logger = LoggerFactory.getLogger(getClass)

  def allTeams: List[Team]

  def allUsers: List[User]
  def allUsersFrom(t: Team): List[User] = allUsers filter (_.team == t)

  def allMissAppointments: List[MissAppointment]

  def allMissAppointments(t: Team): List[MissAppointment] = allMissAppointments.filter(_.user.team == t).sorted
  def allMissAppointments(u: User): List[MissAppointment] = allMissAppointments.filter(_.user == u).sorted

  def update(miss: MissAppointment) = {
    /* no-op */
    logger.debug("Updating MissAppointment {}", miss)
  }

  def lastWorklogFrom(user: User): List[Worklog]
  
  //
  
  def allHolidays : List[Holiday]
  
  def insert(hol: Holiday)
  
  def delete(hol: Holiday)

}

object InMemory extends Repository {

  var environment = Environment1

  def allTeams = environment.teamList

  def allUsers = environment.userList

  def allMissAppointments = environment.missAppointmentList

  def lastWorklogFrom(user: User) = environment.worklogList.filter(_.user == user)

  def shutdown = logger.debug("Shutdown")
  
  def allHolidays : List[Holiday] = environment.allHolidays
  
  def insert(hol: Holiday) = environment.insert(hol)
  
  def delete(hol: Holiday) = environment.delete(hol)

}
