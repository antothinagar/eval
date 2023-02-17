package controllers

import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents, Result}
import repositories.User
import services.{Logging, SubscriptionService}

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.util.control.NonFatal

class SubscriptionController @Inject()(subscriptionService: SubscriptionService, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) with Logging {

  def registerUser = Action.async { request =>
    val params = request.body.asJson.get
    info("params::: " + params)
    val userName = params("name").as[String]
    val emailId = params("email_id").as[String]
    val isActive = params("is_active").as[Boolean]
    val password = params("password").as[String]
    subscriptionService.registerUser(User(userName, emailId, isActive, password))
      .map(userId => Ok(Json.obj("status" -> "success", "message" -> s"user got register with $userId", "response" -> 200)))
      .recover {
        recoverBlock
      }
  }

  def addOrUpdateAnnualPlan = Action.async { request =>
    val params = request.body.asJson.get
    val userId = params("user_id").as[Int]
    val planName = params("plan_name").as[String]
    subscriptionService.subscribeForAPlan(userId, planName)
      .map(_ => Ok(Json.obj("status" -> "success", "message" -> s"user has subscribed for the plan", "response" -> 200)))
      .recover {
        recoverBlock
      }
  }

  def addChannel = Action.async { request =>
    val params = request.body.asJson.get
    val userId = params("user_id").as[Int]
    val channelName = params("channel_name").as[String]
    val channelLanguage = params("channel_language").as[String]
    subscriptionService.addAdditionalChannel(userId, channelName, channelLanguage)
      .map(_ => Ok(Json.obj("status" -> "success", "message" -> s"user has subscribed for the channel", "response" -> 200)))
      .recover {
        recoverBlock
      }
  }

  def recoverBlock: PartialFunction[Throwable, Result] = {
    case NonFatal(th) =>
      error(th.getMessage, th)
      InternalServerError("There is some error with the process")
  }


}
