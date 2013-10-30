package bedex

import scala.collection.Map
import scala.util.Properties
import org.slf4j.LoggerFactory
import bedex.biz.Domain
import bedex.biz.InMemory
import bedex.biz.jdbc.H2Repository
import bedex.biz.jdbc.OracleRepository
import scalafx.application.JFXApp.Parameters
import scalafx.application.JFXApp
import scala.collection.mutable.ArrayBuffer

object Bootstrap {

  def apply(params: Parameters) = new Bootstrap(params)

  private val shutdownAgents = new ArrayBuffer[ShutdownAgent]

  def register(shut: ShutdownAgent) {
    shutdownAgents += shut
  }

  private def shutdown = shutdownAgents.foreach(_.shutdown())

}

trait ShutdownAgent {

  def shutdown()

}

// FIXME use class "scala.util.Either" instead of throwing exceptions !?
class Bootstrap private (params: Parameters) extends ShutdownAgent {

  private val logger = LoggerFactory.getLogger(getClass)

  private val DB_URL = "db.url"
  private val DB_USER = "db.user"
  private val DB_PASSWD = "db.passwd"
  private val DB_FILE_NAME = "/db.properties"

  logger.debug("Running with java.version={}, java-vm.version={}, from={}",
    Properties.javaVersion,
    Properties.javaVmVersion,
    Properties.javaVendor)

  // init

  initRepository()
  initFXStuff()

  // defs

  def initRepository() {
    if (params.named.contains(DB_URL)) parseNamedParams(params)
    else if (dbFileExists) parseDBFile
    else inMemory

    Bootstrap.register(Domain.repository)
  }

  def initFXStuff() {
    JFXApp.AUTO_SHOW = false
  }

  def shutdown() = {
    Bootstrap.shutdown
    logger.info("Shutting down")
  }

  private def parseNamedParams(params: Parameters) = {
    logger.debug("Bootstraping with named parameters")
    extractParams(params.named)
  }

  private def extractParams(map: Map[String, String]) = {
    val url = map(DB_URL)
    val user = map.getOrElse(DB_USER, null)
    val passwd = map.getOrElse(DB_PASSWD, null)

    connectTo(url, user, passwd)
  }

  private def connectTo(url: String, user: String, passwd: String) {
    if (url.startsWith("jdbc:oracle:thin:@")) thinOracleOrCry(url, user, passwd)
    else if (url.startsWith("jdbc:h2:")) h2OrCry(url, user, passwd)
    else throw new Error(s"Data source ${url} not available yet")
  }

  private def dbFileExists = getClass.getResourceAsStream(DB_FILE_NAME) != null

  private def parseDBFile = {
    val props = new java.util.Properties
    props.load(Bootstrap.getClass.getResourceAsStream(DB_FILE_NAME))

    logger.debug("Bootstraping with properties file {}", DB_FILE_NAME)

    val propz = scala.collection.JavaConversions.propertiesAsScalaMap(props)

    if (propz.contains(DB_URL)) extractParams(propz)
    else throw new Error(s"Properties file '${DB_FILE_NAME}' is invalid")
  }

  private def h2OrCry(url: String, user: String, passwd: String) {
    logger.info("Connecting to H2 at url={}, user={}", Seq(url, user): _*)
    //H2Repository.connectTo(url, user, passwd)
    //Domain.repository = H2Repository
    Domain.repository = new H2Repository(url, user, passwd)
  }

  private def thinOracleOrCry(url: String, user: String, passwd: String) {
    logger.info("Connecting to Oracle at url={}, user={}", Seq(url, user): _*)
    OracleRepository.connectTo(url, user, passwd)
    Domain.repository = OracleRepository
  }

  private def inMemory() {
    logger.debug("Bootstraping with default environment in memory")
    Domain.repository = InMemory
  }
}
