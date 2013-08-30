package bedex.biz

import java.sql.ResultSet
import java.sql.Connection
import java.sql.PreparedStatement

package object jdbc {

  import scala.language.implicitConversions

  class ResultSetIterator(rs: ResultSet) extends Iterator[ResultSet] {
    def hasNext: Boolean = rs.next()
    def next(): ResultSet = rs
  }

  implicit def resultSetToResultSetIterator(rs: ResultSet) = new ResultSetIterator(rs)

  class StringOps(val in: String) {

    def nullToEmpty: String = {
      if (in == null) "" else in
    }
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
			WHERE u.team = t.name AND w.user_name = u.user_name
    			AND u.user_name = ?
			ORDER BY w.worked_date ASC"""

  // jdbc

  def query[T](sql: String)(functor: ResultSet => T)(implicit connection: Connection): List[T] = {
    val stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
    val rs = stmt.executeQuery(sql)

    // (for (row <- new ResultSetIterator(rs)) yield functor(row)).toList
    new ResultSetIterator(rs).map(functor).toList
  }

   def preparedQuery[T](sql: String)
   		(prepare: PreparedStatement => Unit)
   		(functor: ResultSet => T)
  		(implicit connection: Connection): List[T] = {

    val stmt = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)

    prepare(stmt)

    val rs = stmt.executeQuery()

    // (for (row <- new ResultSetIterator(rs)) yield functor(row)).toList
    new ResultSetIterator(rs).map(functor).toList
  }

  //def executeUpdate(sql: String)(prepare: PreparedStatement => Unit = (_: PreparedStatement) => ())(implicit connection: Connection) = {
  def executeUpdate(sql: String)(prepare: PreparedStatement => Unit)(implicit connection: Connection) = {
    val stmt = connection.prepareStatement(sql)
    prepare(stmt)
    stmt.executeUpdate
    connection.commit
  }

  def fulFillStatement(stmt: PreparedStatement, miss: MissAppointment) = {
    stmt.setString(1, miss.reason.get)
    stmt.setString(2, miss.typeLog.toString.drop(1))
    stmt.setInt(3, miss.levelLog)
    stmt.setDate(4, new java.sql.Date(miss.endDate.getTime))
    stmt.setString(5, miss.user.name)
  }

  implicit def nullStringToBlank(in: String) = new StringOps(in)

  // biz conversion

  def incarnateTeam(rs: ResultSet) = Team(rs.getString("t_name"), rs.getString("t_coach"), rs.getString("t_manager"))

  def incarnateUser(rs: ResultSet) = User(rs.getString("u_user_name"), incarnateTeam(rs))

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
}
