package repositories

import com.google.inject.ImplementedBy

import java.util.Date
import scala.concurrent.Future

class UserPlanRepo extends UserPlanRepoAPI with MySQLDBComponent


@ImplementedBy(classOf[UserPlanRepo])
trait UserPlanRepoAPI extends UserPlanTable {
  this: DBComponent =>

  import driver.api._

  def addUserPlan(userId: Int, planId: Int): Future[Int] = {
    db.run(autoInc += UserPlan(userId, planId))
  }

  def getPlanId(userId: Int): Future[Option[Int]] = {
    db.run(userPlanTableQuery.filter(_.userId === userId).map(_.planId).result.headOption)
  }

}


trait UserPlanTable extends UserTable with PlanTable {
  this: DBComponent =>

  import DateMapper._
  import driver.api._

  type UserPlanTableType = UserPlanTable
  val userPlanTableQuery = TableQuery[UserPlanTableType]
  val autoInc = userPlanTableQuery returning userPlanTableQuery.map(_.id)

  class UserPlanTable(tag: Tag) extends Table[UserPlan](tag, "user_plan") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val userId = column[Int]("user_id")
    val planId = column[Int]("plan_id")
    val created = column[Date]("created")
    val updated = column[Date]("updated")


    def * = (userId, planId, created, updated, id) <> (UserPlan.tupled, UserPlan.unapply)


    def userPlan = foreignKey("userPlan_fKey", userId, userTableQuery)(_.id)

    def myPlan = foreignKey("plan_fkey", planId, planTableQuery)(_.id)

  }


}

case class UserPlan(userId: Int, planId: Int, created: Date = new Date,
                    updated: Date = new Date, id: Int = 0)


