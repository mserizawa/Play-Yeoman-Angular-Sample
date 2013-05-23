package controllers

import play.api._
import play.api.mvc._
import play.api.libs.oauth.OAuthCalculator
import play.api.libs.oauth.OAuthCalculator
import play.api.libs.ws.WS
import play.api.libs.concurrent.Execution.Implicits._

object Application extends Controller {
  
  def index = Action { implicit request =>
    Twitter.sessionTokenPair match {
      case Some(credentials) =>
        Async {
          WS.url("https://api.twitter.com/1.1/statuses/home_timeline.json")
            .sign(OAuthCalculator(Twitter.KEY, credentials))
            .get
            .map(result => Ok(result.json))
        }
      case _ => Redirect(routes.Twitter.authenticate)
    }
  }

  def getFollowers(cursor: String) = Action { implicit request =>
    Twitter.sessionTokenPair match {
      case Some(credentials) =>
        Async {
          WS.url("https://api.twitter.com/1.1/followers/list.json?include_user_entities=false&cursor=" + cursor)
            .sign(OAuthCalculator(Twitter.KEY, credentials))
            .get
            .map(result => Ok(result.json))
        }
      case _ => Redirect(routes.Twitter.authenticate)
    }
  }
  
  def javascriptRoutes = Action { implicit request =>
    import routes.javascript._
    Ok(
      Routes.javascriptRouter("jsRoutes")(
          routes.javascript.Application.index,
          routes.javascript.Application.getFollowers
    )).as("text/javascript")
  }

}