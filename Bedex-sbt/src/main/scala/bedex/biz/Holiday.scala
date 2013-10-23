package bedex.biz

import java.util.Date
import Domain.repository

case class Holiday(val name: String, val when: Date) 

object Holiday {

  def all = repository.allHolidays

  def insert(hol: Holiday) = repository.insert(hol)

  def delete(hol: Holiday) = repository.delete(hol)

}