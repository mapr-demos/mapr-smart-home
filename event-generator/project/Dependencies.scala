import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4"
  lazy val snakeYaml = "org.yaml" % "snakeyaml" % "1.19"
  lazy val gson = "com.google.code.gson" % "gson" % "2.8.2"
  lazy val maprStreams = "com.mapr.streams" % "mapr-streams" % "6.0.0-mapr"
  lazy val commonsLogging = "commons-logging" % "commons-logging" % "1.2"
}
