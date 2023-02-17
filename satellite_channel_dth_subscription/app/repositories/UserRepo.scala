package repositories

import com.google.inject.ImplementedBy

import scala.concurrent.Future

@ImplementedBy(classOf[UserRepo])
trait UserRepoAPI extends UserTable {
  this: DBComponent =>

  def addNewUser(user: User): Future[Int] = {
    db.run(userAutoInc += user)
  }

}


trait UserTable {
  this: DBComponent =>

  import driver.api._

  type UserTableType = UserTable
  val userTableQuery = TableQuery[UserTableType]
  val userAutoInc = userTableQuery returning userTableQuery.map(_.id)

  class UserTable(tag: Tag) extends Table[User](tag, "users") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val name = column[String]("name")
    val emailId = column[String]("email_id")
    val isActive = column[Boolean]("is_active")
    val password = column[String]("password")

    def * = (name, emailId, isActive, password, id) <> (User.tupled, User.unapply)

    def uniqueEmail = index("unique_email", emailId, unique = true)

  }

}

class UserRepo extends UserRepoAPI with MySQLDBComponent

case class User(name: String, emailId: String, isActive: Boolean, password: String, id: Int = 0)

