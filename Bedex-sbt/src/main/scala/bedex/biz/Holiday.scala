package bedex.biz

import java.util.Date
import Domain.repository
import javafx.beans.property.SimpleStringProperty

case class Holiday(val name: String, val when: Date) 

object Holiday {

  def all = repository.allHolidays

  def insert(hol: Holiday) = repository.insert(hol)

  def delete(hol: Holiday) = repository.delete(hol)

}