package services

import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.specs2.mock.Mockito
import repositories.{ChannelRepoAPI, PackageChannelRepoAPI, PackageRepoAPI, PlanRepoAPI, User, UserPlanRepoAPI, UserRepoAPI}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SubscriptionServiceSpec extends PlaySpec with GuiceOneAppPerSuite with Mockito with ScalaFutures {


  val mockedUserRepo = mock[UserRepoAPI]
  val mockedUserPlanRepo = mock[UserPlanRepoAPI]
  val mockedPlanRepo = mock[PlanRepoAPI]
  val mockedPackageRepoAPI = mock[PackageRepoAPI]
  val mockedChannelRepo = mock[ChannelRepoAPI]
  val mockedPackageChannelRepo = mock[PackageChannelRepoAPI]
  val testService = new SubscriptionServiceImpl(mockedUserRepo, mockedUserPlanRepo, mockedPlanRepo,
    mockedPackageRepoAPI, mockedChannelRepo, mockedPackageChannelRepo)

  "Subscription Service" should {
    "able to register new User" in {
      val user=User("surekha","surekha202@gmail.com",true,"abc@123")
      mockedUserRepo.addNewUser(user) returns Future.successful(1)
      mockedUserPlanRepo.addUserPlan(1,1) returns  Future.successful(1)
      val result = testService.registerUser(User("surekha","surekha202@gmail.com",true,"abc@123"))
      result mustBe Some(1)
    }
  }

}
