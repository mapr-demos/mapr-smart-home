import Dependencies._

lazy val root = (project in file(".")).
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

    libraryDependencies += sparkStreamingKafka,
    libraryDependencies += sparkCore,
    libraryDependencies += scalaTest % Test,
    libraryDependencies += sparkStreaming ,
    libraryDependencies += maprStreams,
    libraryDependencies += maprDbOjai,
    libraryDependencies += sparkSql,
    libraryDependencies += playJson

  )
