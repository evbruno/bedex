package bedex.view

import scala.collection.mutable.ArrayBuffer
import scala.language.implicitConversions
import bedex.biz.MissAppointment
import bedex.biz.MissAppointments
import bedex.biz.NoTeam
import bedex.biz.NoUser
import bedex.biz.Team
import bedex.biz.Teams
import bedex.biz.User
import bedex.biz.Users
import scalafx.collections.ObservableBuffer
import org.slf4j.LoggerFactory

abstract class FilterOption(val name: String) {
  override def toString = name
}
case object FilterAll extends FilterOption("All")
case object FilterWithReason extends FilterOption("With reason")
case object FilterWithoutReason extends FilterOption("Without reason")

// TODO rename to Model ?
class Controller {

  import scala.language.{ implicitConversions, postfixOps }

  private lazy val logger = LoggerFactory.getLogger(getClass)

  private implicit def missAppointmentListToObsservableBuffer(miss: List[MissAppointment]) = ObservableBuffer[MissAppointment](miss)

  def teams: ObservableBuffer[Team] = ObservableBuffer((NoTeam :: Teams.all) toSeq)

  def users: ObservableBuffer[User] = ObservableBuffer((NoUser :: Users.all) toSeq)

  def filterOptions: ObservableBuffer[FilterOption] =
    ObservableBuffer(FilterAll, FilterWithReason, FilterWithoutReason)

  def usersFrom(team: Team) =
    if (team == NoTeam) users
    else ObservableBuffer((NoUser +: Users.findBy(team)) toSeq)

  //def missApointments = MissAppointments all

  def missApointments(team: Team, user: User): ObservableBuffer[MissAppointment] =
    if (user != null && user != NoUser) {
      logger.debug("Searching for user {}", user)
      MissAppointments findBy user
    } else if (team != null && team != NoTeam) {
      logger.debug("Searching for team {}", team)
      MissAppointments findBy team
    } else {
      logger.debug("Showing everybody")
      MissAppointments all
    }

  private lazy val dirty = ObservableBuffer(ArrayBuffer[MissAppointment]())

  lazy val isDirty = {
    val ret = new javafx.beans.property.SimpleBooleanProperty(dirty.isEmpty)
    dirty onChange { ret set (dirty.isEmpty) }
    ret
  }

  def putDirty(miss: MissAppointment) = {
    dirty += miss
  }

  def save() = {
    logger.debug("Saving ...")
    for (m <- dirty) {
      logger.debug("Saving {} with new reason {} for {}", m.user.name, m.reason.get, m.user)
      m.commit()
    }
    dirty.clear()
  }

  def cancel() = {
    logger.debug("Canceling ...")
    for (m <- dirty) {
      logger.debug("Canceling op {} with new reason {} for {}", m.user.name, m.reason.get, m.user)
      m.rollback()
    }
    dirty.clear()
  }

}