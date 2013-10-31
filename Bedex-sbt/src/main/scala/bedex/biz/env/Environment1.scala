package bedex.biz.env

import java.util.Date
import scala.io.Source
import bedex.biz._
import java.text.SimpleDateFormat
import java.io.File
import org.slf4j.LoggerFactory
import scala.collection.mutable.Buffer
import bedex.core.Logger

// FIXME define with override from Repository
object Environment1 extends Logger {

  // Repository API 

  // readonly

  def teamList: List[Team] = teams.toList

  def userList: List[User] = users.toList

  def missAppointmentList: List[MissAppointment] = logs.toList

  def worklogList: List[Worklog] = worklogs.toList

  // holliday

  def allHolidays: List[Holiday] = holidayBuffer.toList

  def insert(hol: Holiday) = holidayBuffer += hol

  def delete(hol: Holiday) = holidayBuffer -= hol

  // vacation

  def allVacations: List[Vacation] = vacationBuffer.toList
  
  def insert(vacation: Vacation) = vacationBuffer += vacation
  
  def delete(vacation: Vacation) = vacationBuffer -= vacation

  // private fields

  private lazy val holidayBuffer: Buffer[Holiday] = holidays.toBuffer

  private lazy val vacationBuffer: Buffer[Vacation] = vacations.toBuffer

  // helpers

  private def linesFor(source: String): Array[Array[String]] = {
    val inputStream = getClass.getResourceAsStream("/" + source)
    val content = Source.fromInputStream(inputStream, "utf-8").mkString
    content.split("\n").drop(1).map(_.split('|'))
  }

  private lazy val teams: Array[Team] =
    for (line <- linesFor("export_team.dsv")) yield {
      val team = Team(line(0), line(1))
      debug("Team loaded: {}", team)
      team
    }

  private lazy val users: Array[User] =
    for (line <- linesFor("export_user_team.dsv")) yield {
      val user = User(line(0), teams.find(_.name == line(1)).get)
      //debug("User loaded: {}", user)
      user
    }

  private lazy val dateFormatter = new SimpleDateFormat("dd-MM-yy")

  private lazy val logs: Array[MissAppointment] =
    for (line <- linesFor("export_log.dsv")) yield {
      val user = users.find(_.name == line(0)).get
      val levelLog = line(1).toInt
      val typeLog = Symbol(line(2))
      val worked = line(3).toFloat
      val startDate = dateFormatter.parse(line(4))
      val endDate = dateFormatter.parse(line(5))
      val reason = if (line.length > 8) line(8) else ""

      val miss = MissAppointment(user, startDate, endDate, worked, typeLog, reason, levelLog)
      //debug("Log loaded: {} ", miss)
      miss
    }

  private lazy val worklogs: Array[Worklog] =
    for (line <- linesFor("export_worklog.dsv")) yield {
      val user = users.find(_.name == line(0)).get
      val date = dateFormatter.parse(line(1))
      val worked = line(2).toFloat

      Worklog(user, date, worked)
    }

  private lazy val holidays: Array[Holiday] =
    for (line <- linesFor("export_holiday.dsv")) yield {
      val name = line(1)
      val date = dateFormatter.parse(line(0))

      val holiday = Holiday(name, date)
      debug("Holiday: {}", holiday)
      holiday
    }

  private lazy val vacations: Array[Vacation] =
    for (line <- linesFor("export_vacation.dsv")) yield {
      val user = users.find(_.name == line(0)).get
      val startDate = dateFormatter.parse(line(1))
      val endDate = dateFormatter.parse(line(2))
      val reason = line(3)

      val vacation = Vacation(user, reason, startDate, endDate)
      debug("Vacation: {}", vacation)
      vacation
    }
}