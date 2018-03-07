package com.mapr.demos

import play.api.libs.json.{Json, OWrites, Reads}

case class Event(_id: Option[String], timestamp: Long, homeId: String, sensorId: String, metrics: Map[String, String])

object Event extends MapRDBDocument[Event] {

  implicit val eventWrites: OWrites[Event] = Json.writes[Event]
  implicit val eventReads: Reads[Event] = Json.reads[Event]

  private[demos] def tablePath: String = "/apps/events"

  private[demos] def jsonStringToModel(jsonString: String): Option[Event] = {
    val jsValue = Json.parse(jsonString)
    Json.fromJson(jsValue).fold(
      invalid => None,
      valid => Some(valid)
    )
  }

  private[demos] def modelToJsonString(model: Event): String = Json.toJson(model).toString()

}

