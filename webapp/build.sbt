name := """webapp"""
organization := "com.mapr.demos"

version := "1.0-SNAPSHOT"

lazy val webapp = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"

resolvers += "mapr-releases" at "http://repository.mapr.com/maven/"
resolvers += "JBoss 3rd-party" at "https://repository.jboss.org/nexus/content/repositories/thirdparty-releases/"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

libraryDependencies += "com.mapr.ojai" % "mapr-ojai-driver" % "6.0.0-mapr"
libraryDependencies += "com.mapr.db" % "maprdb" % "6.0.0-mapr"
libraryDependencies += "com.mapr.streams" % "mapr-streams" % "6.0.0-mapr"

assemblyMergeStrategy in assembly := {
  case r if r.startsWith("reference.conf") => MergeStrategy.concat
  case PathList("META-INF", m) if m.equalsIgnoreCase("MANIFEST.MF") => MergeStrategy.discard
  case x => MergeStrategy.first
}
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.mapr.demos.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.mapr.demos.binders._"
