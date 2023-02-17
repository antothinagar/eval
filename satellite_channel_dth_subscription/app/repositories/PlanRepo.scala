package repositories

import com.google.inject.ImplementedBy

import scala.concurrent.Future

class PlanRepo extends PlanRepoAPI with MySQLDBComponent

@ImplementedBy(classOf[PlanRepo])
trait PlanRepoAPI extends PlanTable {
  this: DBComponent =>

  import driver.api._

  def addPlanDetails(planDetail: Plan): Future[Int] = {
    db.run(planAutoInc += planDetail)
  }

  def getPlanDuration(planId: Int): Future[Option[String]] = {
    db.run(planTableQuery.filter(_.id === planId).map(_.planDuration).result.headOption)
  }


  def getPlanIdByPackageId(packId: Int): Future[Option[Int]] = {
    db.run(planTableQuery.filter(_.packageId === packId).map(_.id).result.headOption)
  }

}


trait PlanTable extends PackageTable {
  this: DBComponent =>

  import driver.api._

  type PlanTableType = PlanTable
  val planTableQuery = TableQuery[PlanTableType]
  val planAutoInc = planTableQuery returning planTableQuery.map(_.id)

  class PlanTable(tag: Tag) extends Table[Plan](tag, "plan") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val cost = column[Int]("cost")
    val packageId = column[Int]("package_id")
    val planDuration = column[String]("plan_duration")

    def * = (cost, packageId, planDuration, id) <> (Plan.tupled, Plan.unapply)

    def myPackage = foreignKey("packagePlan_fkey", packageId, packageTableQuery)(_.id)

  }

}

case class Plan(cost: Int, packageId: Int, planDuration: String, id: Int = 0)