package repositories

import com.typesafe.config.ConfigFactory
import slick.jdbc.MySQLProfile

trait MySQLDBComponent extends DBComponent {
  override val driver = MySQLProfile
  import driver.api._
  val db= MySQLDb.connectionPool

}
object MySQLDb{
  import slick.jdbc.MySQLProfile.api._
  val connectionPool=Database.forConfig("dbconf",ConfigFactory.load().withFallback(ConfigFactory.load("satellite_channel_dth_subscription")))
}
