name := """webapp"""
organization := "com.mapr.demos"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

resolvers += "mapr-releases" at "http://repository.mapr.com/maven/"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

libraryDependencies += "com.mapr.ojai" % "mapr-ojai-driver" % "6.0.0-mapr"
libraryDependencies += "com.mapr.db" % "maprdb" % "6.0.0-mapr"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.mapr.demos.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.mapr.demos.binders._"
