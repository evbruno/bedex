package bedex.biz

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

package object jdbc {

  class StringOps(val in: String) {

    def nullToEmpty: String = {
      if (in == null) "" else in
    }
  }

  class ResultSetIterator(rs: ResultSet) extends Iterator[ResultSet] {
    def hasNext: Boolean = rs.next()
    def next(): ResultSet = rs
  }

  import scala.language.implicitConversions

  implicit def resultSetToResultSetIterator(rs: ResultSet) = new ResultSetIterator(rs)

  def query[T](sql: String)(functor: ResultSet => T)(implicit connection: Connection): List[T] = {
    val stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
    val rs = stmt.executeQuery(sql)

    new ResultSetIterator(rs).map(functor).toList
  }

  def preparedQuery[T](sql: String)(prepare: PreparedStatement => Unit)(functor: ResultSet => T)(implicit connection: Connection): List[T] = {

    val stmt = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)

    prepare(stmt)

    val rs = stmt.executeQuery()

    new ResultSetIterator(rs).map(functor).toList
  }

  def executeUpdate(sql: String)(prepare: PreparedStatement => Unit)(implicit connection: Connection) = {
    val stmt = connection.prepareStatement(sql)
    prepare(stmt)
    stmt.executeUpdate
    connection.commit
  }
}
