package com.mapr.demos

import play.api.libs.json.{Json, OWrites, Reads}

case class Home(_id: Option[String], name: String, address: String)

object Home extends MapRDBDocument[Home] {

  implicit val homeWrites: OWrites[Home] = Json.writes[Home]
  implicit val homeReads: Reads[Home] = Json.reads[Home]

  private[demos] def tablePath: String = "/apps/homes"

  private[demos] def jsonStringToModel(jsonString: String): Option[Home] = {
    val jsValue = Json.parse(jsonString)
    Json.fromJson(jsValue).fold(
      invalid => None,
      valid => Some(valid)
    )
  }

  private[demos] def modelToJsonString(model: Home): String = Json.toJson(model).toString()

}

