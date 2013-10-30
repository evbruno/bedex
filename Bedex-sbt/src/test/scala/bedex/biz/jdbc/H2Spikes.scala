package bedex.biz.jdbc

import java.sql.{ Connection, DriverManager, ResultSet, SQLException }
import Helper._
import bedex.biz.env.Environment1
import java.text.SimpleDateFormat

object H2Spikes extends App {

  // Load the driver
  Class forName "org.h2.Driver"

  // Setup the connection
  val url = "jdbc:h2:~/test_env"

  implicit val conn = DriverManager.getConnection(url)
  conn.setAutoCommit(true)

  try {
    // CREATE TABLES

    executeSql("""
      create table TEAM (
    		name VARCHAR(50),
    		coach VARCHAR(50),
    		manager VARCHAR(50)
    	)
        """)

    executeSql("""
        create table USER_TEAM (
    		user_name VARCHAR(50),
    		team VARCHAR(50),
    		end_date DATE
        )
        """)

    executeSql("""
    	CREATE TABLE LOGMISSAPPOINTMENT (
    		USER_NAME VARCHAR(50),
			COACH VARCHAR(50),
			MANAGER VARCHAR(50),
			START_DATE DATE,
			END_DATE DATE,
			TYPE_LOG VARCHAR(5),
			LEVEL_LOG NUMBER,
			WORKED NUMBER,
			EXPECTED NUMBER,
			MSG VARCHAR(1000),
			MSGTIME DATE,
			REASON VARCHAR(1000)
   ) """)

    executeSql("""
    	CREATE TABLE WORKLOG_SUMMARY (
		    USER_NAME VARCHAR(50),
		    WORKED_DATE DATE,
			WORKED_TIME NUMBER
   ) """)

    executeSql("""
	    CREATE TABLE HOLIDAY (
		   DAY DATE NOT NULL, 
		   NAME VARCHAR(50) NOT NULL, 
	) """)

    // INSERTS

    println("--------- Inserting TEAMS")

    val teamsSql = for (team <- Environment1.teamList)
      yield "INSERT INTO TEAM (name, coach) VALUES ('%s', '%s')".format(team.name, team.coach)

    executeBatchSql(teamsSql)

    println("--------- Inserting Users")

    val usersSql = for (user <- Environment1.userList)
      yield "INSERT INTO USER_TEAM (user_name, team) VALUES ('%s', '%s')".format(user.name, user.team.name)

    executeBatchSql(usersSql)

    println("--------- Inserting MissAppointmens")

    val missSql = for (miss <- Environment1.missAppointmentList)
      yield """INSERT INTO LOGMISSAPPOINTMENT (user_name, start_date, end_date, worked, type_log, reason)
    		VALUES ('%s', '%s', '%s', '%s', '%s', '%s')"""
      .format(miss.user.name, toS(miss.startDate), toS(miss.endDate), miss.worked, miss.typeLog.toString.drop(1), miss.reason.get)

    executeBatchSql(missSql)

    println("--------- Inserting Worklogs")

    val logsSql = for (worklog <- Environment1.worklogList)
      yield """INSERT INTO WORKLOG_SUMMARY (user_name, worked_date, worked_time)
    	VALUES ('%s', '%s', '%s')"""
      .format(worklog.user.name, toS(worklog.date), worklog.worked)

    executeBatchSql(logsSql)
    
    println("--------- Inserting Holidays")
    
    val logsHol = for (worklog <- Environment1.allHolidays)
    	yield """INSERT INTO HOLIDAY (name, day)
    	VALUES ('%s', '%s')"""
    	.format(worklog.name.replaceAll("'", ""), toS(worklog.when))
    	
    	executeBatchSql(logsHol)

  } finally {
    conn.close
  }

}

object Helper {

  def toS(date: java.util.Date): String = new java.text.SimpleDateFormat("YYYY-MM-dd").format(date)

  def executeBatchSql(sqls: List[String])(implicit connection: Connection) = {
    val statement = connection.createStatement

    for (sql <- sqls) {
      statement.addBatch(sql)
      println(">> Running dml: " + sql)
    }

    statement.executeBatch()

  }

  def executeSql(sql: String)(implicit conn: Connection) = {
    try {
      val statement = conn.createStatement
      statement.execute(sql)
      println(">> Running ddl:" + sql)
    } catch {
      case e: SQLException => println("SQL error: %s" format e.getMessage)
    }

  }

}