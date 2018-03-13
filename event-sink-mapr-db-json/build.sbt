lazy val eventMapRDBSink = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.mapr.demos",
      scalaVersion := "2.11.6",
      version := "0.1.0-SNAPSHOT"
    )),
    name := "event-mapr-db-sink",
    resolvers += "mapr-releases" at "http://repository.mapr.com/maven/",
    resolvers += "JBoss 3rd-party" at "https://repository.jboss.org/nexus/content/repositories/thirdparty-releases/",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % Test,
    libraryDependencies +=  "com.mapr.streams" % "mapr-streams" % "6.0.0-mapr",
    libraryDependencies += "commons-logging" % "commons-logging" % "1.2",
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.8",
    libraryDependencies += "com.mapr.ojai" % "mapr-ojai-driver" % "6.0.0-mapr",
    libraryDependencies += "com.mapr.db" % "maprdb" % "6.0.0-mapr"
)

assemblyMergeStrategy in assembly := {
  case r if r.startsWith("reference.conf") => MergeStrategy.concat
  case PathList("META-INF", m) if m.equalsIgnoreCase("MANIFEST.MF") => MergeStrategy.discard
  case x => MergeStrategy.first
}