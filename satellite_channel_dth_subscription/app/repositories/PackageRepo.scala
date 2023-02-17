package repositories

import com.google.inject.ImplementedBy

import scala.concurrent.Future

class PackageRepo extends PackageRepoAPI with MySQLDBComponent


@ImplementedBy(classOf[PackageRepo])
trait PackageRepoAPI extends PackageTable {
  this: DBComponent =>

  import driver.api._

  def findPackageIdByName(packageName: String): Future[Option[Int]] = {
    db.run(packageTableQuery.filter(_.name === packageName).map(_.id).result.headOption)
  }

  def addPackageDetail(name: String): Future[Int] = {
    db.run(packageAutoInc += PackageType(name))

  }


}


trait PackageTable {
  this: DBComponent =>

  import driver.api._

  type PackageTableType = PackageTable
  val packageTableQuery = TableQuery[PackageTableType]
  val packageAutoInc = packageTableQuery returning packageTableQuery.map(_.id)

  class PackageTable(tag: Tag) extends Table[PackageType](tag, "packages") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val name = column[String]("name")

    def * = (name, id) <> (PackageType.tupled, PackageType.unapply)

    def uniqueName = index("unique_name", name, unique = true)


  }

}

case class PackageType(name: String, id: Int = 0)

