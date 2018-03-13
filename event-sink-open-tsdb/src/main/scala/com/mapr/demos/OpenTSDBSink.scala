package com.mapr.demos

import java.util.{Collections, Properties}

import com.mapr.demos.OpenTSDBSink.props
import com.mapr.demos.TSDBRecord.MetricName
import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.json.{Json, OWrites, Reads}

import scala.annotation.tailrec

object OpenTSDBSink {

  val ConsumerPollTimeoutMs = 100
  val ConsumingIntervalMs = 500

  val log : Logger = LoggerFactory.getLogger(OpenTSDBSink.getClass)

  val props = new Properties()
  props.load(getClass.getResourceAsStream("/application.properties"))

  val consumerProperties = new Properties
  consumerProperties.setProperty("group.id", "event-sink-open-tsdb")
  consumerProperties.setProperty("auto.offset.reset", "latest")
  consumerProperties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  consumerProperties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

  val consumer = new KafkaConsumer[String, String](consumerProperties)

  implicit val eventWrites: OWrites[Event] = Json.writes[Event]
  implicit val eventReads: Reads[Event] = Json.reads[Event]

  var tsdbHost = "localhost"

  def main(args: Array[String]): Unit = {

        tsdbHost = if(args.length > 0) args(0) else props.getProperty("tsd.host")

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
        val records = TSDBRecord.fromEvent(event)
        log.debug(s"Records stored: \n${records.mkString("\n")}")
        TSDBRecordWriter.store(tsdbHost, records)
      })

    Thread.sleep(ConsumingIntervalMs)
    consume()
  }

}

//put smart.home.event 1407165399000 99 metric=temperature home=553d1f54-8956-4452-9e98-b7981fa133da sensor=586a6d46-e891-4aeb-8f94-d4efac836df2
case class TSDBRecord(timestamp: Long, value: String, metricTag: String, homeTag: String, sensorTag: String) {
  override def toString: String = s"$MetricName $timestamp $value metric=$metricTag home=$homeTag sensor=$sensorTag"
}

object TSDBRecord {

  val MetricName = "smart.home.event"

  def fromEvent(event: Event): Seq[TSDBRecord] = {
    event.metrics.map(m => TSDBRecord(event.timestamp, m._2, m._1, event.homeId, event.sensorId)).toSeq
  }

}

object TSDBRecordWriter {
  def store(host: String, records: Seq[TSDBRecord]): Unit = {

    import java.io._
    import java.net._

    val s = new Socket(InetAddress.getByName(host), 4242)
    val out = new PrintStream(s.getOutputStream)

    records.map(r => s"put $r").foreach(r => out.println(r))

    out.flush()
    s.close()
  }
}