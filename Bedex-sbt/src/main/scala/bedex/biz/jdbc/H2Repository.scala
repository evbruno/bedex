package bedex.biz.jdbc

import java.sql._
import bedex.biz._

// Spiking only (:
object H2Repository extends Repository {

  private implicit var conn: Connection = _

  def connectTo(url: String, user: String, passwd: String) = {
    Class.forName("org.h2.Driver")
    conn = DriverManager.getConnection(url, user, passwd)
    conn.setAutoCommit(false)
  }

  def allTeams: List[Team] =  query(defaultSelectTeamSQL)(incarnateTeam)

  def allUsers: List[User] = query(defaultSelectUserSQL)(incarnateUser)

  def allMissAppointments: List[MissAppointment] = 
    query(defaultSelectMissAppointmentsSQL)(incarnateMissAppointment)
    
  def lastWorklogFrom(user: User) = null

  override def update(miss: MissAppointment): Unit = {
    executeUpdate(defaultUpdateMissAppointmentSQL) {
      fulFillStatement(_, miss)
    }
  }

  def shutdown() = {
    conn.close
  }

}
