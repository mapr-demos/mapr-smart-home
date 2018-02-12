package com.mapr.demos

import java.util.Properties

import com.mapr.demos.producer.KafkaEventProducer
import com.mapr.demos.util.HomeDescriptorParser

object EventGenerator {

  val props = new Properties()
  props.load(getClass.getResourceAsStream("/application.properties"))

  def main(args: Array[String]): Unit = {

    val home = HomeDescriptorParser.fromResources("/home.yml")
    val topic = props.getProperty("topic")

    new KafkaEventProducer(topic, home).start()
  }

}
