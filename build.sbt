lazy val root = (project in file("."))
  .aggregate(eventGenerator, eventProcessor, eventMapRDBSink, eventOpenTSDBSink, webapp)

lazy val eventGenerator = (project in file("event-generator"))

lazy val eventProcessor = (project in file("event-processor"))

lazy val eventMapRDBSink = (project in file("event-sink-mapr-db-json"))

lazy val eventOpenTSDBSink = (project in file("event-sink-open-tsdb"))

lazy val webapp = (project in file("webapp"))
