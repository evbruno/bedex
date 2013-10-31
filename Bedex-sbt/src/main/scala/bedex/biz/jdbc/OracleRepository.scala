package bedex.biz.jdbc

import java.sql._
import bedex.biz._

// FIXME current database impl. does not contain any pks
class OracleRepository(val url: String,
  private val user: String,
  private val passwd: String) extends Repository with JDBCCommonRepo {

  Class.forName("oracle.jdbc.driver.OracleDriver")

  implicit val connection = DriverManager.getConnection(url, user, passwd)
  connection.setAutoCommit(false)

  // AND m.start_date > SYSDATE - ???
  override def allMissAppointments: List[MissAppointment] = {
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

  override def shutdown() = {
    logger.info("Desconecting from {}", url)
    connection.close
  }

}