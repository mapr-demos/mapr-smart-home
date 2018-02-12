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
    libraryDependencies += scalaTest % Test,
    libraryDependencies += snakeYaml
  )
