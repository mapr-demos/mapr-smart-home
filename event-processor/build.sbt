//import Dependencies._

lazy val eventProcessor = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.mapr.demos",
      scalaVersion := "2.11.6",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Event Processor",
    resolvers += "mapr-releases" at "http://repository.mapr.com/maven/",
    resolvers += "JBoss 3rd-party" at "https://repository.jboss.org/nexus/content/repositories/thirdparty-releases/",

    dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % "2.7.2",
    dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.7.2",
    dependencyOverrides += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.7.2",

    libraryDependencies +=  "org.apache.spark" %% "spark-streaming-kafka-0-9" % "2.1.0-mapr-1801",
    libraryDependencies += "org.apache.spark" %% "spark-core" % "2.1.0-mapr-1801",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % Test,
    libraryDependencies +=  "org.apache.spark" % "spark-sql_2.11" % "2.1.0-mapr-1801" ,
    libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.1.0-mapr-1801",
    libraryDependencies += "com.mapr.streams" % "mapr-streams" % "6.0.0-mapr",
    libraryDependencies += "com.mapr.ojai" % "mapr-ojai-driver" % "6.0.0-mapr",
    libraryDependencies +=  "com.typesafe.play" % "play-json_2.11" % "2.6.8"

  )
