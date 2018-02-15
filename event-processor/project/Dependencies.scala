import sbt._

object Dependencies {
  lazy val sparkStreamingKafka = "org.apache.spark" %% "spark-streaming-kafka-0-9" % "2.1.0-mapr-1707"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4"
  lazy val sparkCore = "org.apache.spark" %% "spark-core" % "2.1.0-mapr-1707"
  lazy val sparkStreaming = "org.apache.spark" %% "spark-streaming" % "2.1.0-mapr-1707"
  lazy val maprStreams = "com.mapr.streams" % "mapr-streams" % "6.0.0-mapr"

}
