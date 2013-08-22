package bedex.biz

import java.util.Date
import scalafx.beans.property.StringProperty
import scalafx.beans.property.BooleanProperty
import bedex.beans.RollbackableStringProperty

object Domain {

  implicit val TeamOrdering = Ordering.by {
    t: Team => (t.name, t.coach)
  }

  implicit val MissAppointmentOrdering = Ordering.by {
    m: MissAppointment => (m.user, m.startDate)
  }

  var repository: Repository = InMemory

}

import Domain._

// I dont like toString (:
trait Displayable {

  def display: String

}

// Team

// FIXME manager != null
case class Team(val name: String, val coach: String, val manager: String = null) extends Displayable {

  def display = name
}

object NoTeam extends Team(null, null, null) {

  override def display = "<No Team>"

}

object Teams {

  def all = repository.allTeams

}

// User

case class User(val name: String, val team: Team) extends Ordered[User] with Displayable {

  def compare(other: User) = name compare other.name

  def display = name

}

object NoUser extends User(null, null) {

  override def display = "<No User>"

  override def compare(other: User) = -1

}

object Users {

  def all: List[User] = repository.allUsers

  def findBy(from: Team): List[User] = repository.allUsersFrom(from)

}

// Miss Appointments

// TODO Rollbackable?
// TODO add fields l_expected

case class MissAppointment(
  val user: User,
  val startDate: Date,
  val endDate: Date,
  val worked: Float,
  val typeLog: Symbol = 'LOW,
  private val reason_ : String,
  val levelLog: Int) {

  assert(reason_ != null, "I dont like null reasons. Please, send me an empty string instead")
  assert(Array('LOW, 'HIGH) contains (typeLog))

  val reason = new RollbackableStringProperty(this, "reason", reason_)

  def commit() = {
    MissAppointments.update(this)
    reason.commit
  }

  def rollback() = {
    reason.rollback
  }

  def isDirty: Boolean = reason.isDirty

}

object MissAppointments {

  import scala.language.postfixOps

  def all = repository allMissAppointments

  def findBy(team: Team) = repository allMissAppointments team

  def findBy(user: User) = repository allMissAppointments user

  def update(miss: MissAppointment) = repository update miss

}
