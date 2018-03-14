lazy val eventProcessor = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.mapr.demos",
      scalaVersion := "2.11.6",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "event-processor",
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

assemblyMergeStrategy in assembly := {
  case PathList("com", "mapr", "demos", xs @ _*) => MergeStrategy.last
  case PathList("org","aopalliance", xs @ _*) => MergeStrategy.last
  case PathList("javax", "inject", xs @ _*) => MergeStrategy.last
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
  case PathList("javax", "activation", xs @ _*) => MergeStrategy.last
  case PathList("org", "jboss", xs @ _*) => MergeStrategy.last
  case PathList("io", "netty", xs @ _*) => MergeStrategy.last
  case PathList("javassist", xs @ _*) => MergeStrategy.last
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case PathList("com", "google", xs @ _*) => MergeStrategy.last
  case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
  case PathList("com", "codahale", xs @ _*) => MergeStrategy.last
  case PathList("com", "yammer", xs @ _*) => MergeStrategy.last
  case "about.html" => MergeStrategy.rename
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
  case "META-INF/mailcap" => MergeStrategy.last
  case "META-INF/mimetypes.default" => MergeStrategy.last
  case "plugin.properties" => MergeStrategy.last
  case "log4j.properties" => MergeStrategy.last
  case "git.properties" => MergeStrategy.last
  case "META-INF/drill-module.conf" => MergeStrategy.last
  case "drill-module.conf" => MergeStrategy.last
  case "META-INF/drill-module-scan/registry.json" => MergeStrategy.last
  case "META-INF/io.netty.versions.properties" => MergeStrategy.last
  case "META-INF/libnetty-transport-native-epoll.so" => MergeStrategy.last
  case "META-INF/native/libnetty-transport-native-epoll.so" => MergeStrategy.last
  case "overview.html" => MergeStrategy.last  // Added this for 2.1.0 I think
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
