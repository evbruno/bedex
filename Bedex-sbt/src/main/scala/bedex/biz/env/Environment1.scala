package bedex.biz.env

import java.util.Date
import scala.io.Source
import bedex.biz.MissAppointment
import bedex.biz.Team
import bedex.biz.User
import bedex.biz.MissAppointment
import java.text.SimpleDateFormat
import java.io.File
import org.slf4j.LoggerFactory

object Environment1 {

  private lazy val logger = LoggerFactory.getLogger(getClass)

  def teamList: List[Team] = teams.toList
  def userList: List[User] = users.toList
  def missAppointmentList: List[MissAppointment] = logs.toList

  private def linesFor(source: String): Array[Array[String]] = {
    val inputStream = getClass.getResourceAsStream("/" + source)
    val content = Source.fromInputStream(inputStream, "utf-8").mkString
    for (line <- content.split("\n").drop(1)) yield line.split('|')
  }

  private lazy val teams: Array[Team] =
    for (line <- linesFor("export_team.dsv")) yield {
      val team = Team(line(0), line(1))
      logger.debug("Team loaded: {}", team)
      team
    }

  private lazy val users: Array[User] =
    for (line <- linesFor("export_user_team.dsv")) yield {
      val user = User(line(0), teams.find(_.name == line(1)).get)
      logger.debug("User loaded: {}", user)
      user
    }

  private lazy val logs: Array[MissAppointment] =
    for (line <- linesFor("export_log.dsv")) yield {
      val user: User = users.find(_.name == line(0)).get
      val startDate: Date = new SimpleDateFormat("dd-MMM-YY").parse(line(4))
      val endDate: Date = new SimpleDateFormat("dd-MMM-YY").parse(line(4))
      val worked: Float = line(3).toFloat
      val reason: String = if (line.length > 8) line(8) else ""
      val typeLog = Symbol(line(2))

      val miss = MissAppointment(user, startDate, endDate, worked, typeLog, reason)
      logger.debug("Log loaded: {}", miss)
      miss
    }

}