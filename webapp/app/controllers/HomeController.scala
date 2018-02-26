package controllers

import javax.inject.{Inject, Singleton}

import model.Home
import play.api.libs.json._
import play.api.mvc._

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  implicit val homeWrites: OWrites[Home] = Json.writes[Home]
  implicit val homeReads: Reads[Home] = Json.reads[Home]

  /**
    * Returns the list of all existing smart homes.
    *
    * @return the list of all existing smart homes.
    */
  def list = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(Home.list))
  }

  /**
    * Retrieves single smart home by it's identifier.
    *
    * @param id smart home's identifier.
    * @return smart home, which corresponds to specified identifier.
    */
  def get(id: String) = Action { implicit request: Request[AnyContent] =>

    Home.findById(id) match {
      case Some(c) => Ok(Json.toJson(c))
      case None => NotFound(Json.obj("error" -> s"Home with id '$id' not found"))
    }
  }

  /**
    * Deletes single smart home by it's identifier.
    *
    * @param id smart home's identifier.
    * @return appropriate status and error message(in case of failure).
    */
  def delete(id: String) = Action { implicit request: Request[AnyContent] =>

    if (Home.exists(id)) {
      Home.delete(id)
      NoContent
    } else NotFound(Json.obj("error" -> s"Home with id '$id' not found"))
  }

  /**
    * Adds new home.
    *
    * @return created home.
    */
  def add = Action { implicit request: Request[AnyContent] =>

    request.body.asJson match {
      case Some(homeJson) => Json.fromJson[Home](homeJson).fold(
        _ => BadRequest(Json.obj("error" -> "Can not instantiate home from request body")),
        valid => {
          val createdHome = Home.insert(valid)
          Created(Json.toJson(createdHome))
        }
      )

      case None => BadRequest(Json.obj("error" -> "Request body cannot be treated as JSON"))
    }
  }

  /**
    * Updates single smart home.
    *
    * @param id smart home's identifier.
    * @return updated smart home.
    */
  def update(id: String) = Action { implicit request: Request[AnyContent] =>

    if (Home.exists(id)) {
      request.body.asJson match {
        case Some(homeJson) => Json.fromJson[Home](homeJson).fold(
          _ => BadRequest(Json.obj("error" -> "Can not instantiate home from request body")),
          valid => {
            val updatedHome = Home.update(id, valid)
            Ok(Json.toJson(updatedHome))
          }
        )

        case None => BadRequest(Json.obj("error" -> "Request body cannot be treated as JSON"))
      }
    } else NotFound(Json.obj("error" -> s"Home with id '$id' not found"))
  }

}
