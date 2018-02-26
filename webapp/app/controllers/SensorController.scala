package controllers

import javax.inject.{Inject, Singleton}

import model.Sensor
import play.api.libs.json._
import play.api.mvc._

@Singleton
class SensorController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  implicit val sensorWrites: OWrites[Sensor] = Json.writes[Sensor]
  implicit val sensorReads: Reads[Sensor] = Json.reads[Sensor]

  /**
    * Returns the list of all existing metric sensors.
    *
    * @return the list of all existing metric sensors.
    */
  def list = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(Sensor.list))
  }

  /**
    * Retrieves single metric sensor by it's identifier.
    *
    * @param id sensor's identifier.
    * @return sensor, which corresponds to specified identifier.
    */
  def get(id: String) = Action { implicit request: Request[AnyContent] =>

    Sensor.findById(id) match {
      case Some(c) => Ok(Json.toJson(c))
      case None => NotFound(Json.obj("error" -> s"Sensor with id '$id' not found"))
    }
  }

  /**
    * Deletes single sensor by it's identifier.
    *
    * @param id sensor's identifier.
    * @return appropriate status and error message(in case of failure).
    */
  def delete(id: String) = Action { implicit request: Request[AnyContent] =>

    if (Sensor.exists(id)) {
      Sensor.delete(id)
      NoContent
    } else NotFound(Json.obj("error" -> s"Sensor with id '$id' not found"))
  }

  /**
    * Adds new metric sensor.
    *
    * @return created sensor.
    */
  def add = Action { implicit request: Request[AnyContent] =>

    request.body.asJson match {
      case Some(sensorJson) => Json.fromJson[Sensor](sensorJson).fold(
        _ => BadRequest(Json.obj("error" -> "Can not instantiate sensor from request body")),
        valid => {
          val created = Sensor.insert(valid)
          Created(Json.toJson(created))
        }
      )

      case None => BadRequest(Json.obj("error" -> "Request body cannot be treated as JSON"))
    }
  }

  /**
    * Updates single sensor.
    *
    * @param id sensor's identifier.
    * @return updated sensors.
    */
  def update(id: String) = Action { implicit request: Request[AnyContent] =>

    if (Sensor.exists(id)) {
      request.body.asJson match {
        case Some(sensorJson) => Json.fromJson[Sensor](sensorJson).fold(
          _ => BadRequest(Json.obj("error" -> "Can not instantiate sensor from request body")),
          valid => {
            val updated = Sensor.update(id, valid)
            Ok(Json.toJson(updated))
          }
        )

        case None => BadRequest(Json.obj("error" -> "Request body cannot be treated as JSON"))
      }
    } else NotFound(Json.obj("error" -> s"Sensor with id '$id' not found"))
  }

}
