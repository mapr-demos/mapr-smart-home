package com.mapr.demos.producer

import com.mapr.demos.domain.Home

import scala.annotation.tailrec

class KafkaEventProducer(topic: String, home: Home) {

  private val ThreadSleepTimeout = 10

  def start(): Unit = {
    produce(0)
  }

  @tailrec
  private def produce(millis: Long): Unit = {

    import collection.JavaConverters._
    val metricsToBeSent = home.sensors.asScala.flatMap(s => s.metrics.asScala).filter(m => millis % m.intervalMs == 0)

    // TODO send via Kafka Producer
    metricsToBeSent.foreach(println)

    Thread.sleep(ThreadSleepTimeout)
    produce(millis + ThreadSleepTimeout)
  }
}
