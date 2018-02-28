package com.mapr.demos.producer

import java.util.Properties

import play.api.libs.json.{Json, OWrites}

import com.mapr.demos.domain.{Event, HomeDescriptor}
import com.mapr.demos.util.HomeEventsGenerator
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.annotation.tailrec

class KafkaEventProducer(topic: String, home: HomeDescriptor, generatingIntervalMs: Long) {

  implicit val eventWrites: OWrites[Event] = Json.writes[Event]

  private val producerProperties = new Properties
  producerProperties.put("bootstrap.servers", "willbeignored:9092")
  producerProperties.put("client.id", "MapRSmartHomeEventProducer")
  producerProperties.put("batch.size", "0") // Note that disabling batching may cause performance issues
  producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  producerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](producerProperties)

  def start(): Unit = {
    produce(0)
  }

  @tailrec
  private def produce(millis: Long): Unit = {

    HomeEventsGenerator.events(home, millis).map(e => Json.toJson(e).toString()).foreach(json => {
      producer.send(new ProducerRecord[String, String](topic, json))
      println(s"Event sent: $json")
    })

    Thread.sleep(generatingIntervalMs)
    produce(millis + generatingIntervalMs)
  }
}
