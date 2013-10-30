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

  // default JDBC

  def allHolidays: List[Holiday] = query(defaultSelectAllHolidaysSQL)(incarnateHoliday)

  private def holidayStmt(hol: Holiday, rs: PreparedStatement) {
    rs.setString(1, hol.name)
    rs.setDate(2, new java.sql.Date(hol.when.getTime))
  }

  def delete(hol: Holiday) = executeUpdate(defaultDeleteHolidaySQL)(holidayStmt(hol, _))

  def insert(hol: Holiday) = executeUpdate(defaultInsertHolidaySQL)(holidayStmt(hol, _))

  def allVacations: List[Vacation] = query(defaultSelectAllVacationsSQL)(incarnateVacation)

  private def vacationStmt(vac: Vacation, rs: PreparedStatement) {
    rs.setString(1, vac.user.name)
    rs.setString(2, vac.reason)
    rs.setDate(3, new java.sql.Date(vac.startDate.getTime))
    rs.setDate(4, new java.sql.Date(vac.endDate.getTime))
  }

  def insert(vacation: Vacation) = executeUpdate(defaultInsertVacationSQL)(vacationStmt(vacation, _))

  def delete(vacation: Vacation) = executeUpdate(defaultDeleteVacationSQL)(vacationStmt(vacation, _))

}
