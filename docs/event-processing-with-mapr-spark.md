# Real-time event processing and visualization with Spark, MapR Streams, MapR-DB, OpenTSDB and Grafana

## Contents

* [Overview](#overview)
* [The Scenario](#the-scenario)
* [Event Sources](#event-sources)
* [MapR Streams](#mapr-streams)
* [Spark Event Processor](#spark-event-processor)
* [MapR-DB Sink](#mapr-db-sink)
* [OpenTSDB Sink](#opentsdb-sink)
* [MapR-DB](#mapr-db)
* [Webapp Backend](#webapp-backend)
* [Web Client](#web-client)
* [Grafana](#grafana)
* [MapR-DB Grafana Plugin](#mapr-db-grafana-plugin)
* [Summary](#summary)


## Overview

Nowadays, the Internet of Things is a source of a large amount of data. Every day, various devices and sensors that 
make up the Internet of Things gather that data and allow to do an analysis on it. Often in order to extract the 
valuable information, it's critical to make an analysis on data when it is still in motion. In such cases, processing 
systems must be properly designed to be robust, low latency and secure. MapR Converged Data Platform, which integrates 
Hadoop, Spark, and Apache Drill with real-time database capabilities, global event streaming, and scalable enterprise 
storage, will be a good choice while developing such event-processing system. This blog post covers an example of the 
event-processing system - 
[MapR Smart Home Demo application](https://github.com/mapr-demos/mapr-smart-home).

## The Scenario

Registered smart home has one or multiple sensors, each of them can produce multiple metrics to MapR Stream. These 
metrics processed by Event Processor(Spark Streaming Job). Spark Event Processor checks if metric event violates 
condition(s), specified by the user via UI of the Web application. Conditions are stored at MapR-DB JSON Tables. If 
event violates some condition, Spark Job will send a notification to MapR Stream. Web app backend listens to 
`notifications` topic of MapR Stream and sends alerts for each notification to the UI, so the user will be notified if 
some sensor produces suspicious values. 
Thus, if the user wants to monitor boiler temperature he has to do the following:

1. Register the home via Web UI. 
2. Register Boiler temperature sensor.
3. Add condition for Boiler temperature sensor. (for example, `metrics.temperature > 90`)
4. In the case when some sensor's event violates the condition, the user will be notified via Web UI.

Moreover, all events are stored in MapR-DB JSON Tables and/or OpenTSDB and in both cases can be visualized using Grafana.
Below you can see the architecture diagram of the MapR Smart Home System.

![](images/smart-home-design.png?raw=true "System design")

Let's consider each of components in a more detailed way.

### Event Sources

Single instance of [event-generator](https://github.com/mapr-demos/mapr-smart-home/tree/master/event-generator) app
corresponds to single Smart Home. [Event generator](https://github.com/mapr-demos/mapr-smart-home/tree/master/event-generator) 
is Scala application, which generates events according to 
[home.yml](https://github.com/mapr-demos/mapr-smart-home/blob/master/event-generator/src/main/resources/home.yml) 
descriptor file and publishes them to MapR Stream using Kafka Producer API.

Below you can see an example of `home.yml` descriptor file, which defines smart home with single Boiler temperature 
sensor:
```
id: 553d1f54-8956-4452-9e98-b7981fa133da
name: Smart Home 1
sensors:
-
  id: 501c21a6-829a-4b1a-84f1-2d2f3c161fe1
  name: Boiler temperature sensor
  metrics:
  -
    name: temperature
    intervalMs: 1560
    strategy:
      incrementalRange:
        from: 0.5
        to: 100
        step: 7
```

As you can see, the sensor will produce `temperature` metric events with `incrementalRange` strategy: metric's value 
will be increased with constant step starting from specified value in the defined range. Also, `randomRange` strategy 
is available.

Sample event:
```
{
   "timestamp": 1522089794010,
   "homeId": "553d1f54-8956-4452-9e98-b7981fa133da",
   "sensorId": "501c21a6-829a-4b1a-84f1-2d2f3c161fe1",
   "metrics": {
      "temperature": "0.5"
   }
}
```

### MapR Streams

System design image above depicts as events produced by separate home(the instance of event-generator) published to MapR 
Stream. A stream is a collection of topics that you can manage together by:     
1. Setting security policies that apply to all topics in that stream
2. Setting a default number of partitions for each new topic that is created in the stream
3. Set a time-to-live for messages in every topic in the stream

MapR Smart Home System uses single `/apps/smart-home-stream` stream with `notifications` and `events-*` topics.
Each Home has its own events topic in `events-{home id}` format. Such design allows processing data in a more 
efficient way. [Spark DStream](https://spark.apache.org/docs/2.2.0/streaming-programming-guide.html#discretized-streams-dstreams) 
will consist of a number of RDDs which corresponds to the number of topics, each of RDD will consist of a number of 
partitions that corresponds to topic's partitions. All that guarantees an adequate degree of parallelism.

### Spark Event Processor

Event processor is Spark Streaming Job, which receives events from MapR Stream and checks if they violate conditions, 
stored at MapR-DB. Condition documents fetched from MapR-DB using 
[OJAI Driver](https://maprdocs.mapr.com/60/MapR-DB/Indexes/ojai-and-indexes.html). If event violates the condition, 
Spark Job will send a notification to `notifications` topic of MapR 
Stream, using Kafka Producer API. 

Sample notification:
```
{
   "event": {
      "homeId": "4bd50e08-e2e2-475b-a295-c3bc202a430f",
      "sensorId": "501c21a6-829a-4b1a-84f1-2d2f3c161fe1",
      "metrics": {
         "temperature": "73.5"
      }
   },
   "condition": "metrics.temperature > 50",
   "homeName": "Some Smart Home",
   "sensorName": "Boiler temperature sensor"
}
```

The job is written on Scala because such jobs are more efficient, brief and readable. 
Furthermore, Spark itself is built on Scala, so things are "more natural" using Scala.

### MapR-DB Sink

MapR-DB Sink is a simple Scala application which uses Kafka Consumer API and 
[OJAI Driver](https://maprdocs.mapr.com/60/MapR-DB/Indexes/ojai-and-indexes.html) in order to receive and store events 
in MapR-DB JSON Tables. It consumes events directly from MapR Streams as they are published by event generators. 
MapR-DB Sink is a separate application, so it can be run standalone or along with 
[OpenTSDB Sink](#opentsdb-sink). Moreover, if you don't want to store original events, you are free to not run sinks at 
all.

Sample events, stored by sink:
```
$ mapr dbshell

maprdb mapr:> jsonoptions --pretty true
maprdb mapr:> find /apps/events

        ...

{
  "_id" : "ffc19284-ca2f-48b0-aa2c-41d3cdef8eb6",
  "timestamp" : 1521734217953,
  "homeId" : "553d1f54-8956-4452-9e98-b7981fa133da",
  "sensorId" : "501c21a6-829a-4b1a-84f1-2d2f3c161fe1",
  "metrics" : {
    "temperature" : "35.5"
  }
}        
{
  "_id" : "fffffd63-ceff-4ead-91ea-9f72c268bffd",
  "timestamp" : 1521733489264,
  "homeId" : "553d1f54-8956-4452-9e98-b7981fa133da",
  "sensorId" : "586a6d46-e891-4aeb-8f94-d4efac836df2",
  "metrics" : {
    "brightness" : "59"
  }
}
19604 document(s) found.

```

### OpenTSDB Sink

OpenTSDB Sink also uses Kafka Consumer API to receive events from MapR Streams. As [MapR-DB Sink](#mapr-db-sink) it's 
separate application and it should be run if you want to store events' timeseries to OpenTSDB. Event OpenTSDB record has 
the following format:
```
smart.home.event 1407165399000 99 metric=temperature home=553d1f54-8956-4452-9e98-b7981fa133da sensor=586a6d46-e891-4aeb-8f94-d4efac836df2
```
where `smart.home.event` - metric name, `1407165399000` - timestamp, `99` - metric value, `metric` - tag which 
specifies metric name, `home` and `sensor` - tags that specify source sensor and home. 

This format allows OpenTSDB to query metrics using tags in a more efficient way. For example to query value of 
`temperature` metric of `501c21a6-829a-4b1a-84f1-2d2f3c161fe1` sensor for the last year, the following query is used:
```
$ tsdb query 1y-ago last smart.home.event metric=temperature sensor=501c21a6-829a-4b1a-84f1-2d2f3c161fe1

smart.home.event 1521734466785 27.500000 {metric=temperature, sensor=501c21a6-829a-4b1a-84f1-2d2f3c161fe1, home=553d1f54-8956-4452-9e98-b7981fa133da}
smart.home.event 1521734468370 34.500000 {metric=temperature, sensor=501c21a6-829a-4b1a-84f1-2d2f3c161fe1, home=553d1f54-8956-4452-9e98-b7981fa133da}
smart.home.event 1521734469952 41.500000 {metric=temperature, sensor=501c21a6-829a-4b1a-84f1-2d2f3c161fe1, home=553d1f54-8956-4452-9e98-b7981fa133da}
smart.home.event 1521734471536 48.500000 {metric=temperature, sensor=501c21a6-829a-4b1a-84f1-2d2f3c161fe1, home=553d1f54-8956-4452-9e98-b7981fa133da}
smart.home.event 1521734473117 55.500000 {metric=temperature, sensor=501c21a6-829a-4b1a-84f1-2d2f3c161fe1, home=553d1f54-8956-4452-9e98-b7981fa133da}
smart.home.event 1521734474698 62.500000 {metric=temperature, sensor=501c21a6-829a-4b1a-84f1-2d2f3c161fe1, home=553d1f54-8956-4452-9e98-b7981fa133da}
smart.home.event 1521734476278 69.500000 {metric=temperature, sensor=501c21a6-829a-4b1a-84f1-2d2f3c161fe1, home=553d1f54-8956-4452-9e98-b7981fa133da}
smart.home.event 1521734477859 76.500000 {metric=temperature, sensor=501c21a6-829a-4b1a-84f1-2d2f3c161fe1, home=553d1f54-8956-4452-9e98-b7981fa133da}
smart.home.event 1521734479448 83.500000 {metric=temperature, sensor=501c21a6-829a-4b1a-84f1-2d2f3c161fe1, home=553d1f54-8956-4452-9e98-b7981fa133da}
smart.home.event 1521734481029 90.500000 {metric=temperature, sensor=501c21a6-829a-4b1a-84f1-2d2f3c161fe1, home=553d1f54-8956-4452-9e98-b7981fa133da}

...

```

### MapR-DB

[MapR-DB](https://mapr.com/products/mapr-db/) is a high performance NoSQL (“Not Only SQL”) database management system 
built into the MapR Converged Data Platform. It is a highly scalable multi-model database that brings together 
operations and analytics, and real-time streaming and database workloads to enable a broader set of next-generation 
data-intensive applications in organizations.

MapR Smart Home application uses MapR-DB JSON Tables to store registered homes, sensors and conditions. Also, in case 
when MapR-DB Events Sink is running, events are also stored to MapR-DB JSON Table.

```
$ mapr dbshell

maprdb mapr:> jsonoptions --pretty true
maprdb mapr:> find /apps/homes
{
  "_id" : "553d1f54-8956-4452-9e98-b7981fa133da",
  "name" : "Smart Home 1",
  "address" : "Fake street, 1"
}
{
  "_id" : "73a4a391-e3da-43f0-a281-ead30e1157cf",
  "name" : "Some Home",
  "address" : "Some Street"
}
2 document(s) found.

maprdb mapr:> find /apps/sensors
{
  "_id" : "501c21a6-829a-4b1a-84f1-2d2f3c161fe1",
  "home_id" : "553d1f54-8956-4452-9e98-b7981fa133da",
  "name" : "Boiler temperature sensor",
  "conversions" : [ "metrics.temperature > 50" ]
}
{
  "_id" : "586a6d46-e891-4aeb-8f94-d4efac836df2",
  "home_id" : "553d1f54-8956-4452-9e98-b7981fa133da",
  "name" : "Complex multi-metric sensor",
  "conversions" : [ ]
}
2 document(s) found.

```

### Webapp Backend 

Backend part of the webapp receives notifications from MapR Stream using Kafka Consumer API and publishes them to UI via 
WebSocket. It provides REST API for managing smart homes, sensors and conditions. Backend is written using Scala and 
Play Framework. Play is a high-productivity Java and Scala web application framework that integrates the components and 
APIs you need for modern web application development.


### Web Client

Web client is Angular app, which displays registered smart homes, their sensors and corresponding conditions. Also, 
receives and displays notifications of violated conditions.

![](images/smart-homes-dashboard.png?raw=true "Web Client")

### Grafana

Grafana along with MapR-DB JSON Plugin can be used to visualize events, stored at 
MapR-DB JSON Table. Also, its possible to use OpenTSDB Datasource in case when OpenTSDB Sink used.  MapR Smart Home repository contains 
[Grafana Smart Homes Dashboard](https://github.com/mapr-demos/mapr-smart-home/blob/master/grafana-dashboard/smart-homes-dashboard.json)
which is ready to be imported to your Grafana installation.

![](images/grafana-smart-homes-dashboard.png?raw=true "Grafana Smart Homes Dashboard")

### MapR-DB Grafana Plugin

Grafana along with [MapR-DB JSON Datasoutce Plugin](https://github.com/mapr-demos/mapr-db-json-grafana-plugin) is 
intended to visualize arbitrary data stored at MapR-DB JSON tables.

It allows users to:
* create Graph and Table panels 
* visualize raw JSON documents
* specify condition using [MapR-DB Shell](https://maprdocs.mapr.com/60/ReferenceGuide/mapr_dbshell.html) condition syntax 
* specify projection
* use various metric aggregations: Document count, Field value, Field max, Field min, Field avg

## Summary

MapR Converged Data Platform provides a rich set of tools for developing secure, robust and low-latency event-processing 
systems. [MapR Smart Home application](https://github.com/mapr-demos/mapr-smart-home) is an example of event-processing 
system, built on top of MapR Converged Data Platform.
