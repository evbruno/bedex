package bedex

import scalafx.scene.control.TableColumn.CellDataFeatures
import bedex.biz.MissAppointment
import java.text.SimpleDateFormat

package object view {

  type MyColumnFeature = CellDataFeatures[MissAppointment, String]

  import scala.language.implicitConversions

  private lazy val sdf = new SimpleDateFormat("dd/MM/yy")
  implicit def dateToStringCell(date: java.util.Date): String = sdf.format(date)

  implicit def floatToStringCell(value: Float): String = "%.1f (h)".format(value)

}