package com.mapr.demos

import play.api.libs.json.{Json, OWrites, Reads}

case class Sensor(_id: Option[String], home_id: String, name: String, conversions: Seq[String])

object Sensor extends MapRDBDocument[Sensor] {

  implicit val conversionWrites: OWrites[Sensor] = Json.writes[Sensor]
  implicit val conversionReads: Reads[Sensor] = Json.reads[Sensor]

  private[demos] def tablePath: String = "/apps/sensors"

  private[demos] def jsonStringToModel(jsonString: String): Option[Sensor] = {
    val jsValue = Json.parse(jsonString)
    Json.fromJson(jsValue).fold(
      invalid => None,
      valid => Some(valid)
    )
  }

  private[demos] def modelToJsonString(model: Sensor): String = Json.toJson(model).toString()

}
