package bedex.view

import scala.collection.mutable.ArrayBuffer
import scala.language.implicitConversions
import bedex.biz._
import scalafx.collections.ObservableBuffer
import org.slf4j.LoggerFactory
import scalafx.beans.property.BooleanProperty

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

  private def missApointments(team: Team, user: User): ObservableBuffer[MissAppointment] =
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

  def missApointments(team: Team, user: User, level: Option[Int]): ObservableBuffer[MissAppointment] = {
    val logs = missApointments(team, user)

    import bedex.biz.Domain._

    logger.debug("Filter logs#size = {} with level = {}", logs.size, level)

    level match {
      case None => logs
      case Some(lvl) => logs.filter(_.levelLog == lvl).sorted
    }
  }

  lazy val worklogsBuffer = ObservableBuffer(ArrayBuffer[Worklog]())

  def lastWorklogsFrom(user: Option[User]) {
    if (worklogsBuffer.isEmpty) {
      worklogsBuffer.appendAll(Worklogs.lastFrom(user))
      logger.debug("Searching for last worklogs from {} => {} found", user, worklogsBuffer.size)
    } else if (worklogsBuffer.head.user != user.getOrElse(null)) {
      worklogsBuffer.clear
      worklogsBuffer.appendAll(Worklogs.lastFrom(user))
      logger.debug("RE-Searching for last worklogs from {} => {} found", user, worklogsBuffer.size)
    } else
      logger.debug("Current user {}", user)
  }

  private lazy val dirtyBuffer = new ReactiveBuffer[MissAppointment]

  lazy val isEmpty: BooleanProperty = dirtyBuffer.isEmptyProperty

  def putDirty(miss: MissAppointment) = {
    dirtyBuffer += miss
  }

  def save() = {
    logger.debug("Saving ...")
    for (m <- dirtyBuffer) {
      logger.debug("Saving {} with new reason {} for {}", m.user.name, m.reason.get, m.user)
      m.commit()
    }
    dirtyBuffer.clear()
  }

  def cancel() = {
    logger.debug("Canceling ...")
    for (m <- dirtyBuffer) {
      logger.debug("Canceling op {} with new reason {} for {}", m.user.name, m.reason.get, m.user)
      m.rollback()
    }
    dirtyBuffer.clear()
  }

}

class ReactiveBuffer[T] extends ObservableBuffer[T] {

  val isEmptyProperty = new BooleanProperty { value = true }

  ReactiveBuffer.this.onChange {
    isEmptyProperty.set(isEmpty)
  }
}
