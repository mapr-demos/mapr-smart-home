import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4"
  lazy val maprStreams = "com.mapr.streams" % "mapr-streams" % "6.0.0-mapr"
  lazy val commonsLogging = "commons-logging" % "commons-logging" % "1.2"
  lazy val playJson = "com.typesafe.play" %% "play-json" % "2.6.8"
  lazy val ojaiDriver = "com.mapr.ojai" % "mapr-ojai-driver" % "6.0.0-mapr"
  lazy val maprDb = "com.mapr.db" % "maprdb" % "6.0.0-mapr"
}
