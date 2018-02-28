package com.mapr.demos

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka09.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import play.api.libs.json.{Json, OWrites, Reads}

import scala.collection.mutable.ListBuffer

object EventProcessor {

  @transient val log: Logger = Logger.getLogger(EventProcessor.getClass)

  implicit val eventReads: Reads[Event] = Json.reads[Event]
  implicit val eventWrites: OWrites[Event] = Json.writes[Event]
  implicit val notificationWrites: OWrites[Notification] = Json.writes[Notification]

  @transient private val producerProperties = new Properties
  producerProperties.put("bootstrap.servers", "willbeignored:9092")
  producerProperties.put("client.id", "MapRSmartHomeNotificationProducer")
  producerProperties.put("batch.size", "0") // Note that disabling batching may cause performance issues
  producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  producerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  @transient val kafkaProducer: KafkaProducer[String, String] = new KafkaProducer[String, String](producerProperties)

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("EventProcessor").setMaster("local[*]")
    val ssc = new StreamingContext(sparkConf, Seconds(1))
    ssc.sparkContext.setLogLevel("ERROR")
    log.setLevel(Level.INFO)

    val kafkaParams = Map(
      "metadata.broker.list" -> "willbeignored:9092",
      "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
      "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer"
    )

    val events = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Set("/apps/test-stream:test"), kafkaParams))


    events.map(_.value()).mapPartitions(jsonStringIterator => {

      val eventList: ListBuffer[Event] = new ListBuffer()

      while (jsonStringIterator.hasNext) {
        val jsonString = jsonStringIterator.next()

        val jsValue = Json.parse(jsonString)
        val event: Option[Event] = Json.fromJson[Event](jsValue).fold(
          invalid => None,
          valid => Some(valid)
        )

        eventList += event.get
      }

      eventList.iterator
    }).foreachRDD(rdd => {

      val spark = SparkSession.builder().config(sparkConf).getOrCreate()
      import spark.implicits._
      val eventsDataset = spark.createDataset(rdd)
      eventsDataset.cache()

      val sensors = Sensor.list

      sensors
        .filter(sensor => sensor.conversions.nonEmpty)
        .foreach(sensor => {

          val condition = sensor.conversions.mkString(" OR ")

          eventsDataset
            .filter(s"homeId = '${sensor.home_id}'")
            .filter(s"sensorId = '${sensor._id.get}'")
            .filter(condition)
            .foreach(violatedEvent => {

              log.info(s"$violatedEvent violates condition: '$condition'")
              val jsonString = Json.toJson(Notification(violatedEvent, condition)).toString()
              kafkaProducer.send(new ProducerRecord[String, String]("/apps/test-stream:notifications", jsonString))

            })

        })

    })

    ssc.start()
    ssc.awaitTermination()
  }

}

case class Event(homeId: String, sensorId: String, metrics: Map[String, String])

case class Notification(event: Event, condition: String)
