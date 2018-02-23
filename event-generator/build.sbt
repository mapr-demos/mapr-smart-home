import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.mapr.demos",
      scalaVersion := "2.12.4",
      version := "0.1.0-SNAPSHOT"
    )),
    name := "Event Generator",
    resolvers += "mapr-releases" at "http://repository.mapr.com/maven/",
    resolvers += "JBoss 3rd-party" at "https://repository.jboss.org/nexus/content/repositories/thirdparty-releases/",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += snakeYaml,
    libraryDependencies += gson,
    libraryDependencies += maprStreams,
    libraryDependencies += commonsLogging
)
