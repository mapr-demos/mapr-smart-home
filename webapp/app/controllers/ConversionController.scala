package controllers

import javax.inject.{Inject, Singleton}

import model.Conversion
import play.api.libs.json._
import play.api.mvc._

@Singleton
class ConversionController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  implicit val conversionWrites: OWrites[Conversion] = Json.writes[Conversion]
  implicit val conversionReads: Reads[Conversion] = Json.reads[Conversion]

  /**
    * Returns the list of all existing metric conversions.
    *
    * @return the list of all existing metric conversions.
    */
  def list = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(Conversion.list))
  }

  /**
    * Retrieves single metric conversion by it's identifier.
    *
    * @param id conversion's identifier.
    * @return conversion, which corresponds to specified identifier.
    */
  def get(id: String) = Action { implicit request: Request[AnyContent] =>

    Conversion.findById(id) match {
      case Some(c) => Ok(Json.toJson(c))
      case None => NotFound(Json.obj("error" -> s"Conversion with id '$id' not found"))
    }
  }

  /**
    * Deletes single conversion by it's identifier.
    *
    * @param id conversion's identifier.
    * @return appropriate status and error message(in case of failure).
    */
  def delete(id: String) = Action { implicit request: Request[AnyContent] =>

    if (Conversion.exists(id)) {
      Conversion.delete(id)
      NoContent
    } else NotFound(Json.obj("error" -> s"Conversion with id '$id' not found"))
  }

  /**
    * Adds new metric conversion.
    *
    * @return created conversion.
    */
  def add = Action { implicit request: Request[AnyContent] =>

    request.body.asJson match {
      case Some(conversionJson) => Json.fromJson[Conversion](conversionJson).fold(
        _ => BadRequest(Json.obj("error" -> "Can not instantiate conversion from request body")),
        valid => {
          Conversion.insert(valid)
          Created
        }
      )

      case None => BadRequest(Json.obj("error" -> "Request body cannot be treated as JSON"))
    }
  }

  /**
    * Updates single conversion.
    *
    * @param id conversion's identifier.
    * @return updated conversions.
    */
  def update(id: String) = Action { implicit request: Request[AnyContent] =>

    if (Conversion.exists(id)) {
      request.body.asJson match {
        case Some(conversionJson) => Json.fromJson[Conversion](conversionJson).fold(
          _ => BadRequest(Json.obj("error" -> "Can not instantiate conversion from request body")),
          valid => {
            Conversion.update(id, valid)
            Ok
          }
        )

        case None => BadRequest(Json.obj("error" -> "Request body cannot be treated as JSON"))
      }
    } else NotFound(Json.obj("error" -> s"Conversion with id '$id' not found"))
  }

}
