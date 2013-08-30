package bedex.biz.jdbc

import java.sql._
import bedex.biz._

// FIXME current database impl. does not contain any pks
object OracleRepository extends Repository {

  implicit private var conn: Connection = _
  private var url: String = _

  def connectTo(url: String, user: String, passwd: String) = {
    Class.forName("oracle.jdbc.driver.OracleDriver")
    conn = DriverManager.getConnection(url, user, passwd)
    conn.setAutoCommit(false)

    this.url = url
  }

  def allTeams: List[Team] = query(defaultSelectTeamSQL)(incarnateTeam)

  def allUsers: List[User] = query(defaultSelectUserSQL)(incarnateUser)

  // AND m.start_date > SYSDATE - ???
  def allMissAppointments: List[MissAppointment] = {
    val sql =
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
				WHERE u.team = t.name
    				AND m.user_name = u.user_name
    				AND m.start_date > (SYSDATE - 14)
				ORDER BY m.start_date DESC"""

    query(sql)(incarnateMissAppointment)
  }

  def lastWorklogFrom(user: User) = defaultLastWorklogFrom(user)

  override def update(miss: MissAppointment): Unit = {
    val sql = """UPDATE LOGMISSAPPOINTMENT l
			SET l.reason = ?
			WHERE
			  l.type_log = ?
			  AND l.level_log = ?
			  AND l.end_date = ?
			  AND l.user_name = ?"""

    executeUpdate(sql) { fulFillStatement(_, miss) }
  }

  def shutdown() = {
    logger.info("Desconecting from {}", url)
    conn.close
  }
}