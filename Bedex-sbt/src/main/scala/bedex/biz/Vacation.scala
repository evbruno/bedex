package bedex.biz

import java.util.Date
import Domain.repository

case class Vacation(val user: User, val reason: String, val startDate: Date, val endDate: Date)

object Vacation {
  
  def all = repository.allVacations
  
  def insert(vacation: Vacation) = repository.insert(vacation)
  
  def delete(vacation: Vacation) = repository.delete(vacation)
  
}
