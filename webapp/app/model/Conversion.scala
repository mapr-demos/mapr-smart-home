package model

case class Conversion(id: Option[Long], metricName: String)

// TODO implement to store Conversions in MapR-DB
object Conversion {

  def findById(id: Long): Option[Conversion] = Some(Conversion(Some(id), "dummy"))

  def insert(metricName: String): Conversion = Conversion(Some(-1), metricName)

  def insert(conversion: Conversion): Conversion = conversion.copy(id = Some(-1))

  def update(id: Long, metricName: String): Option[Conversion] = Some(Conversion(Some(id), metricName))

  def update(id: Long, conversion: Conversion): Option[Conversion] = Some(conversion.copy(id = Some(id)))

  def delete[B](id: Long, success: () => B, notFound: String => B, failure: String => B): B = {

    // TODO implement actual deletion logic
    val status = if (id < 0) "Failure" else if (id > 0) "Success" else "Not Found"

    status match {
      case "Success" => success()
      case "Not Found" => notFound("Some not found message")
      case _ => failure("Some error message")
    }

  }

  def list: Seq[Conversion] = List(Conversion(Some(1), "dummy1"), Conversion(Some(2), "dummy2"), Conversion(Some(3), "dummy3"))

}
