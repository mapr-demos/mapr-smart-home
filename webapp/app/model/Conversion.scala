package model

import play.api.libs.json.{Json, OWrites, Reads}

case class Conversion(_id: Option[String], expression: String)

object Conversion extends MapRDBDocument[Conversion] {

  implicit val conversionWrites: OWrites[Conversion] = Json.writes[Conversion]
  implicit val conversionReads: Reads[Conversion] = Json.reads[Conversion]

  private[model] def tablePath: String = "/apps/conversions"

  private[model] def jsonStringToModel(jsonString: String): Option[Conversion] = {
    val jsValue = Json.parse(jsonString)
    Json.fromJson(jsValue).fold(
      invalid => None,
      valid => Some(valid)
    )
  }

  private[model] def modelToJsonString(model: Conversion): String = Json.toJson(model).toString()

}
