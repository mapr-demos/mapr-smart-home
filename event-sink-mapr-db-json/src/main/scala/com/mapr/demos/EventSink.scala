package com.mapr.demos

import java.util.{Collections, Properties}

import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}
import org.slf4j.LoggerFactory
import play.api.libs.json.{Json, Reads}

import scala.annotation.tailrec

object EventSink {

  val ConsumerPollTimeoutMs = 100
  val ConsumingIntervalMs = 500

  val log = LoggerFactory.getLogger(EventSink.getClass)

  val props = new Properties()
  props.load(getClass.getResourceAsStream("/application.properties"))

  val consumerProperties = new Properties
  consumerProperties.setProperty("group.id", "event-sink-mapr-db-json")
  consumerProperties.setProperty("auto.offset.reset", "latest")
  consumerProperties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  consumerProperties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

  val consumer = new KafkaConsumer[String, String](consumerProperties)
  implicit val eventReads: Reads[Event] = Json.reads[Event]

  def main(args: Array[String]): Unit = {

    consumer.subscribe(Collections.singletonList(props.getProperty("topic")))
    consume()
  }

  @tailrec
  private def consume(): Unit = {
    val records: ConsumerRecords[String, String] = consumer.poll(ConsumerPollTimeoutMs)

    import scala.collection.JavaConverters._

    records.asScala.map(_.value()).map(jsonString => {
      val jsValue = Json.parse(jsonString)

      Json.fromJson(jsValue).fold(
        invalid => {
          log.error(s"Received invalid event. Errors: $invalid")
          None
        },
        valid => Some(valid)
      )
    })
      .filter(_.isDefined)
      .map(_.get)
      .foreach(event => {
        Event.insert(event)
        log.debug(s"Event $event stored")
      })

    Thread.sleep(ConsumingIntervalMs)
    consume()
  }

}
