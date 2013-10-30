package bedex.biz.jdbc

import bedex.biz.Repository
import java.sql.PreparedStatement
import bedex.biz.Holiday
import bedex.biz.Vacation
import java.sql.Connection
import bedex.biz.Team
import bedex.biz.User
import bedex.biz.MissAppointment

trait JDBCCommonRepo extends Repository {

  implicit val connection: Connection

  // Team

  def allTeams: List[Team] = query(defaultSelectTeamSQL)(incarnateTeam)

  // User

  def allUsers: List[User] = query(defaultSelectUserSQL)(incarnateUser)

  // Miss Appointment

  def allMissAppointments: List[MissAppointment] =
    query(defaultSelectMissAppointmentsSQL)(incarnateMissAppointment)

  override def update(miss: MissAppointment): Unit = {
    executeUpdate(defaultUpdateMissAppointmentSQL) {
      fulFillStatement(_, miss)
    }
  }

  // Worklog

  def lastWorklogFrom(user: User) = defaultLastWorklogFrom(user)

  // Holiday

  private def holidayStmt(hol: Holiday, rs: PreparedStatement) {
    rs.setString(1, hol.name)
    rs.setDate(2, new java.sql.Date(hol.when.getTime))
  }

  def allHolidays: List[Holiday] = query(defaultSelectAllHolidaysSQL)(incarnateHoliday)

  def delete(hol: Holiday) = executeUpdate(defaultDeleteHolidaySQL)(holidayStmt(hol, _))

  def insert(hol: Holiday) = executeUpdate(defaultInsertHolidaySQL)(holidayStmt(hol, _))

  def allVacations: List[Vacation] = query(defaultSelectAllVacationsSQL)(incarnateVacation)

  // Vacation

  private def vacationStmt(vac: Vacation, rs: PreparedStatement) {
    rs.setString(1, vac.user.name)
    rs.setString(2, vac.reason)
    rs.setDate(3, new java.sql.Date(vac.startDate.getTime))
    rs.setDate(4, new java.sql.Date(vac.endDate.getTime))
  }

  def insert(vacation: Vacation) = executeUpdate(defaultInsertVacationSQL)(vacationStmt(vacation, _))

  def delete(vacation: Vacation) = executeUpdate(defaultDeleteVacationSQL)(vacationStmt(vacation, _))

  def shutdown() = connection.close

}