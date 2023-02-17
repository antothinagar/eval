package repositories

class UserRepoTest extends MyFunSuite with UserRepoAPI with TestDBComponent {

  test("able to add new user ") {
    val result = addNewUser(User("surekha","surekha202@gmail.com",true,"Test@123",1))
    whenReady(result) { res => assert(res === 1) }
  }
}
