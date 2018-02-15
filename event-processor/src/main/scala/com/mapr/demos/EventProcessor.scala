package com.mapr.demos

import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka09.{ConsumerStrategies, KafkaUtils, LocationStrategies}

object EventProcessor {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("EventProcessor").setMaster("local[*]")
    val ssc = new StreamingContext(sparkConf, Seconds(1))

    val kafkaParams = Map(
      "metadata.broker.list" -> "willbeignored:9092",
      "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
      "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer"
    )

    val events = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Set("/apps/test-stream:test"), kafkaParams))

    events.map(_.value()).print()

    ssc.start()
    ssc.awaitTermination()
  }

}
