package services

import repositories.{ChannelRepoAPI, PackageChannelRepoAPI, PackageRepoAPI, Plan, PlanRepoAPI, User, UserPlanRepoAPI, UserRepoAPI}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class SubscriptionServiceImpl @Inject()(
                                         userRepoAPI: UserRepoAPI,
                                         userPlanRepoAPI: UserPlanRepoAPI,
                                         planRepoAPI: PlanRepoAPI,
                                         packageRepo: PackageRepoAPI,
                                         channelRepoAPI: ChannelRepoAPI,
                                         packageChannelRepoAPI: PackageChannelRepoAPI
                                       )(implicit ec: ExecutionContext) extends SubscriptionService {
  val BASIC_ID = 1

  def registerUser(user: User): Future[Int] = {
    userRepoAPI.addNewUser(user).flatMap { userId => userPlanRepoAPI.addUserPlan(userId, BASIC_ID) }
  }

  def subscribeForAPlan(userId: Int, planName: String): Future[Int] = {
    packageRepo.findPackageIdByName(planName).flatMap { packId =>
      planRepoAPI.getPlanIdByPackageId(packId.getOrElse(BASIC_ID))
        .flatMap { planId => userPlanRepoAPI.addUserPlan(userId, planId.getOrElse(BASIC_ID)) }
    }
  }

  def addAdditionalChannel(userId: Int, channelName: String, channelLanguage: String): Future[Int] = {
    for {
      channel <- channelRepoAPI.getChannelDetails(channelName, channelLanguage)
      packageId <- packageRepo.addPackageDetail(s"add_$channelName-$userId")
      oldPlanId <- userPlanRepoAPI.getPlanId(userId)
      duration <- planRepoAPI.getPlanDuration(oldPlanId.getOrElse(BASIC_ID))
      res <- if (channel.isDefined) packageChannelRepoAPI.insert(packageId, channel.get.id)
        .flatMap { _ =>
          planRepoAPI.addPlanDetails(Plan(channel.get.cost, packageId, duration.getOrElse("30 days")))
            .flatMap { planId => userPlanRepoAPI.addUserPlan(userId, planId) }
        } else Future.successful(0)
    } yield (res)
  }

}
