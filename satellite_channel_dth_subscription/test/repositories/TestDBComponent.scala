package repositories

import com.typesafe.config.ConfigFactory
import services.Logging
import slick.jdbc.H2Profile

import java.util.UUID

trait TestDBComponent extends DBComponent with Logging {

  override val driver = H2Profile

  val db = {
    import driver.api._
    val randomDB = "jdbc:h2:mem:test" + UUID.randomUUID().toString() + ";"
    val config = ConfigFactory.load("test-dbservices")
    val dbDriver = config.getString("db.driver")
    val url = randomDB + config.getString("db.url")
    debug(s"URL :[$url] driver: [$driver]")
    Database.forURL(url, driver = dbDriver)
  }
}
