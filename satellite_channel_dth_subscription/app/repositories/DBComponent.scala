package repositories

import slick.jdbc.JdbcProfile

import java.text.SimpleDateFormat

trait DBComponent {
  val driver:JdbcProfile
  import driver.api._
  val db:Database

  def currentTime(): String = (System.currentTimeMillis() / 1000).toString

  val jan2010UnixTime: String = (new SimpleDateFormat("yyyy-MM-DD").parse("2010-01-01").getTime / 1000).toString

  object DateMapper {

    implicit val utilDate2SqlTimestampMapper = MappedColumnType.base[java.util.Date, java.sql.Timestamp](
      { utilDate => new java.sql.Timestamp(utilDate.getTime()) },
      { sqlTimestamp => new java.util.Date(sqlTimestamp.getTime()) })

  }
}
