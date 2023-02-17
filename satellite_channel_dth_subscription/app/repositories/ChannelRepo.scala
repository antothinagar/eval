package repositories

import com.google.inject.ImplementedBy

import scala.concurrent.Future

class ChannelRepo extends ChannelRepoAPI with MySQLDBComponent

@ImplementedBy(classOf[ChannelRepo])
trait ChannelRepoAPI extends ChannelTable {
  this: DBComponent =>

  import driver.api._

  def getChannelDetails(channelName: String, channelLanguage: String): Future[Option[Channel]] = {
    db.run(channelTableQuery.filter(channel => channel.name === channelName && channel.lang === channelLanguage).result.headOption)
  }

}


trait ChannelTable {
  this: DBComponent =>

  import driver.api._

  type ChannelTableType = ChannelTable
  val channelTableQuery = TableQuery[ChannelTableType]
  val channelAutoInc = channelTableQuery returning channelTableQuery.map(_.id)

  class ChannelTable(tag: Tag) extends Table[Channel](tag, "channels") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val name = column[String]("name")
    val cost = column[Int]("cost")
    val lang = column[String]("language")

    def * = (name, cost, lang, id) <> (Channel.tupled, Channel.unapply)

    def uniqueName = index("unique_name", name, unique = true)
  }

}

case class Channel(name: String, cost: Int, lang: String, id: Int = 0)

case class Package(channels: List[Channel])