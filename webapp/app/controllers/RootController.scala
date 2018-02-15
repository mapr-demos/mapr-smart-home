package controllers

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc._


@Singleton
class RootController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def apiRoot() = Action { implicit request: Request[AnyContent] =>
    Ok(Json.arr()) // TODO list of available routes
  }
}
