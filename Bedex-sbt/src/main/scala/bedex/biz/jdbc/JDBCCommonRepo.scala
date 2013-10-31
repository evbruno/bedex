package bedex.biz.jdbc

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

import bedex.biz.Holiday
import bedex.biz.MissAppointment
import bedex.biz.Repository
import bedex.biz.Team
import bedex.biz.User
import bedex.biz.Vacation
import bedex.biz.Worklog

trait JDBCCommonRepo extends Repository {

  implicit val connection: Connection

  // API

  def allTeams: List[Team] = query(defaultSelectTeamSQL)(incarnateTeam)
  
  def allUsers: List[User] = query(defaultSelectUserSQL)(incarnateUser)

  def allMissAppointments: List[MissAppointment] =
    query(defaultSelectMissAppointmentsSQL)(incarnateMissAppointment)

  override def update(miss: MissAppointment): Unit = {
    executeUpdate(defaultUpdateMissAppointmentSQL) {
      fulFillStatement(_, miss)
    }
  }

  def lastWorklogFrom(user: User) = defaultLastWorklogFrom(user)

  def allHolidays: List[Holiday] = query(defaultSelectAllHolidaysSQL)(incarnateHoliday)

  def delete(hol: Holiday) = executeUpdate(defaultDeleteHolidaySQL)(holidayStmt(hol, _))

  def insert(hol: Holiday) = executeUpdate(defaultInsertHolidaySQL)(holidayStmt(hol, _))

  def allVacations: List[Vacation] = query(defaultSelectAllVacationsSQL)(incarnateVacation)
  
  def insert(vacation: Vacation) = executeUpdate(defaultInsertVacationSQL)(vacationStmt(vacation, _))
  
  def delete(vacation: Vacation) = executeUpdate(defaultDeleteVacationSQL)(vacationStmt(vacation, _))
  
  def shutdown() = connection.close
  
  // local stuff

  private def holidayStmt(hol: Holiday, rs: PreparedStatement) {
	  rs.setString(1, hol.name)
	  rs.setDate(2, new java.sql.Date(hol.when.getTime))
  }

  private def vacationStmt(vac: Vacation, rs: PreparedStatement) {
    rs.setString(1, vac.user.name)
    rs.setString(2, vac.reason)
    rs.setDate(3, new java.sql.Date(vac.startDate.getTime))
    rs.setDate(4, new java.sql.Date(vac.endDate.getTime))
  }

  // constants

  lazy val defaultSelectTeamSQL =
    """SELECT name t_name, coach t_coach, manager t_manager
    		FROM TEAM order by t_name ASC"""

  lazy val defaultSelectUserSQL =
    """SELECT u.user_name u_user_name, u.team t_name, t.coach t_coach, t.manager t_manager
			FROM USER_TEAM  u, TEAM t
			WHERE u.team = t.name
    		ORDER BY u_user_name"""

  lazy val defaultSelectMissAppointmentsSQL =
    """SELECT m.start_date l_start_date,
    				m.end_date l_end_date,
    				m.type_log l_type_log,
    				m.level_log l_level_log,
    				m.worked l_worked,
    				m.expected l_expected,
    				m.reason l_reason,
    				u.user_name u_user_name,
    				u.team t_name,
    				t.coach t_coach,
    				t.manager t_manager
			FROM LOGMISSAPPOINTMENT m, USER_TEAM  u, TEAM t
			WHERE u.team = t.name AND m.user_name = u.user_name
			ORDER BY m.start_date DESC"""

  lazy val defaultUpdateMissAppointmentSQL =
    """UPDATE LOGMISSAPPOINTMENT l
			SET l.reason = ?
			WHERE
			  l.type_log = ?
			  AND l.level_log = ?
			  AND l.end_date = ?
			  AND l.user_name = ?"""

  lazy val defaultSelectWorklogSummarySQL =
    """SELECT w.worked_date,
		            w.worked_time,
    				u.user_name u_user_name,
    				u.team t_name,
    				t.coach t_coach,
    				t.manager t_manager
			FROM WORKLOG_SUMMARY w, USER_TEAM  u, TEAM t
			WHERE u.team = t.name AND w.user_name = u.user_name AND u.end_date IS NULL
    			AND u.user_name = ?
			ORDER BY w.worked_date ASC"""

  lazy val defaultSelectAllHolidaysSQL =
    """SELECT day, name
		FROM holiday
		ORDER BY day DESC """

  lazy val defaultDeleteHolidaySQL =
    """DELETE FROM holiday WHERE name = ? AND day = ? """

  lazy val defaultInsertHolidaySQL =
    """INSERT INTO HOLIDAY (name, day) VALUES (?, ?)"""

  lazy val defaultSelectAllVacationsSQL =
    """SELECT u.user_name u_user_name,
    				u.team t_name,
    				t.coach t_coach,
    				t.manager t_manager,
            		n.start_date, 
            		n.end_date, 
            		n.reason 
  		FROM no_appointment n, USER_TEAM  u, TEAM t
  		WHERE u.team = t.name AND n.user_name = u.user_name AND u.end_date IS NULL
  		ORDER BY end_date DESC """

  lazy val defaultInsertVacationSQL =
    """INSERT INTO NO_APPOINTMENT (user_name, reason, start_date, end_date) 
    	VALUES (?, ?, ?, ?)"""

  lazy val defaultDeleteVacationSQL =
    """DELETE FROM no_appointment WHERE user_name = ? AND reason = ? AND start_date = ? AND end_date = ?"""

  //

  def fulFillStatement(stmt: PreparedStatement, miss: MissAppointment) = {
    stmt.setString(1, miss.reason.get)
    stmt.setString(2, miss.typeLog.toString.drop(1))
    stmt.setInt(3, miss.levelLog)
    stmt.setDate(4, new java.sql.Date(miss.endDate.getTime))
    stmt.setString(5, miss.user.name)
  }

  def defaultLastWorklogFrom(user: User)(implicit connection: Connection) =
    preparedQuery(defaultSelectWorklogSummarySQL)(_.setString(1, user.name))(incarnateWorklog)

  import scala.language.implicitConversions

  private implicit def nullStringToBlank(in: String) = new StringOps(in)

  // biz convertion

  def incarnateTeam(rs: ResultSet) = 
    Team(rs.getString("t_name"), rs.getString("t_coach"), rs.getString("t_manager"))

  def incarnateUser(rs: ResultSet) = 
    User(rs.getString("u_user_name"), incarnateTeam(rs))

  def incarnateMissAppointment(rs: ResultSet) = MissAppointment(
    incarnateUser(rs),
    rs.getDate("l_start_date"),
    rs.getDate("l_end_date"),
    rs.getFloat("l_worked"),
    Symbol(rs.getString("l_type_log")),
    rs.getString("l_reason").nullToEmpty,
    rs.getInt("l_level_log"))

  def incarnateWorklog(rs: ResultSet) = 
    Worklog(incarnateUser(rs), rs.getDate("worked_date"), rs.getFloat("worked_time"))

  def incarnateHoliday(rs: ResultSet) = 
    Holiday(rs.getString("name"), rs.getDate("day"))

  def incarnateVacation(rs: ResultSet) =
    Vacation(incarnateUser(rs), rs.getString("reason"), rs.getDate("start_date"), rs.getDate("end_date"))

}