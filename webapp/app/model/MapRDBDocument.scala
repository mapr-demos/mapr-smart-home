package model

import java.util.UUID

import org.ojai.store.{Connection, DriverManager}

abstract class MapRDBDocument[D] {

  private[model] val idFieldPath: String = "_id"
  private[model] val connection: Connection = DriverManager.getConnection("ojai:mapr:")

  private[model] def tablePath: String

  private[model] def jsonStringToModel(jsonString: String): Option[D]

  private[model] def modelToJsonString(model: D): String

  def findById(id: String): Option[D] = {
    val doc = connection.getStore(tablePath).findById(id)
    if (doc != null) jsonStringToModel(doc.asJsonString()) else None
  }

  def insert(model: D): Unit = {

    val jsonString = modelToJsonString(model)
    val id = UUID.randomUUID().toString
    val doc = connection.newDocument(jsonString)

    connection.getStore(tablePath).insert(id, doc)
  }

  def update(id: String, model: D): Unit = {
    val jsonString = modelToJsonString(model)
    val doc = connection.newDocument(jsonString)
    connection.getStore(tablePath).replace(id, doc)
  }

  def exists(id: String): Boolean = connection.getStore(tablePath).findById(id) != null

  def delete(id: String): Unit = {
    connection.getStore(tablePath).delete(id)
  }

  def list: Seq[D] = {
    import scala.collection.JavaConverters._
    connection.getStore(tablePath).find().iterator().asScala.toSeq
      .map(_.asJsonString())
      .map(jsonStringToModel)
      .filter(_.isDefined)
      .map(_.get)
  }

}
