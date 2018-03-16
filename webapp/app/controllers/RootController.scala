package controllers

import java.util.{Collections, Properties}
import javax.inject._

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Sink, Source}
import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}
import play.api.libs.json.Json
import play.api.mvc._


@Singleton
class RootController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  val consumerProperties = new Properties
  consumerProperties.setProperty("group.id", "webapp-notification-listener")
  consumerProperties.setProperty("auto.offset.reset", "latest")
  consumerProperties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  consumerProperties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

  val consumer = new KafkaConsumer[String, String](consumerProperties)


  def apiRoot() = Action { implicit request: Request[AnyContent] =>
    Ok(Json.arr()) // TODO list of available routes
  }

  def index = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }


  def ws = WebSocket.accept[String, String] { request =>


    consumer.subscribe(Collections.singletonList("/apps/smart-home-stream:notifications"))

    import scala.concurrent.duration._

    // Just ignore the input
    val in = Sink.ignore

    // Send a single 'Hello!' message and close
    val out = Source.tick(1.second, 1.second, NotUsed)
      .map(_ => {


        val records: ConsumerRecords[String, String] = consumer.poll(100)

        val i = records.iterator()

        var notifications: List[String] = List()
        while (i.hasNext) {

          val rec = i.next()
          val jsonString: String = rec.value()

          println("NOTIFICATION: " + jsonString)
          notifications = jsonString :: notifications
        }

        if (notifications.isEmpty) {
          None
        } else {
          Some(notifications.mkString("\n"))
        }
      })
      .filter(_.nonEmpty).map(_.get)

    Flow.fromSinkAndSource(in, out)
  }

}
