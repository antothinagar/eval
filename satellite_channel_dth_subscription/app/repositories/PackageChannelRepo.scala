package repositories

import com.google.inject.ImplementedBy

import scala.concurrent.Future


class PackageChannelRepo extends PackageChannelRepoAPI with MySQLDBComponent


@ImplementedBy(classOf[PackageChannelRepo])
trait PackageChannelRepoAPI extends PackageChannelTable {
  this: DBComponent =>

  import driver.api._

  def insert(packId: Int, channelId: Int): Future[Int] = {
    db.run(packageChannelTableQuery += PackageChannel(packId, channelId))
  }

}


trait PackageChannelTable extends PackageTable with ChannelTable {
  this: DBComponent =>

  import driver.api._

  type PackageChannelTableType = PackageChannelTable
  val packageChannelTableQuery = TableQuery[PackageChannelTableType]

  class PackageChannelTable(tag: Tag) extends Table[PackageChannel](tag, "package_channels") {
    val packageId = column[Int]("package_id")
    val channelId = column[Int]("channel_id")


    def * = (packageId, channelId) <> (PackageChannel.tupled, PackageChannel.unapply)

    def myPackage = foreignKey("package_fKey", packageId, packageTableQuery)(_.id)

    def channel = foreignKey("channel_fkey", channelId, channelTableQuery)(_.id)

  }

}

case class PackageChannel(packageId: Int, channelId: Int)


