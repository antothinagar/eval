package services

import com.google.inject.ImplementedBy
import repositories.User

import scala.concurrent.Future

@ImplementedBy(classOf[SubscriptionServiceImpl])
trait SubscriptionService {

  def registerUser(user: User): Future[Int]

  def subscribeForAPlan(userId: Int, planName: String): Future[Int]

  def addAdditionalChannel(userId: Int, channelName: String, channelLanguage: String): Future[Int]

}
