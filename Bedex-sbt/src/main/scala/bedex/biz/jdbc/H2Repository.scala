package bedex.biz.jdbc

import java.sql._
import bedex.biz._

// Spiking only (:
object H2Repository extends Repository {

  private implicit var conn: Connection = _
  private var url: String = _

  def connectTo(url: String, user: String = null, passwd: String = null) = {
    Class.forName("org.h2.Driver")
    conn = DriverManager.getConnection(url, user, passwd)
    conn.setAutoCommit(false)

    this.url = url
  }

  def allTeams: List[Team] = query(defaultSelectTeamSQL)(incarnateTeam)

  def allUsers: List[User] = query(defaultSelectUserSQL)(incarnateUser)

  def allMissAppointments: List[MissAppointment] =
    query(defaultSelectMissAppointmentsSQL)(incarnateMissAppointment)

  def lastWorklogFrom(user: User) = defaultLastWorklogFrom(user)

  override def update(miss: MissAppointment): Unit = {
    executeUpdate(defaultUpdateMissAppointmentSQL) {
      fulFillStatement(_, miss)
    }
  }

  def shutdown() = {
    logger.info("Desconecting from {}", url)
    conn.close
  }
  
  def allHolidays : List[Holiday] = ???
  
  def update(hol: Holiday) = ???
   
  def insert(hol: Holiday) = ???
  
  def delete(hol: Holiday) = ???

}
