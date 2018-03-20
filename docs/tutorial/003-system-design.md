# System Design

## Contents

* [Overview](#overview)
* [Design](#design)
* [Grafana](#grafana)
* [Streams Design](#streams-design)
* [What's next?](#whats-next)
* [Tutorial links](#tutorial-links)

## Overview
This document describes the architecture of MapR Smart Home Event Processing System.

## Design

MapR Smart Home is an event processing system which consists of the following components:
* Event generator

A single instance of event generator acts as a separate smart home with one or multiple sensors. It publishes metrics 
events to MapR Stream.

* Event processor

Event processor is Spark Streaming Job, which receives events from MapR Stream and checks if they violate conditions, 
stored at MapR-DB.

* Web application backend

Backend part of webapp, receives notifications from MapR Stream and publishes them to UI via WebSocket. Provides REST 
API for managing smart homes, sensors and conditions.

* Web application UI

Angular app, which displays registered smart homes, their sensors and corresponding conditions. Also, receives and 
displays notifications of violated conditions.

* Events MapR-DB JSON Sink

Receives events and stores them at MapR-DB JSON Table.

* Events OpenTSDB Sink

Receives events and stores them at OpenTSDB. Each of sinks can be running standalone or along with another one.


![](../images/smart-home-design.png?raw=true "System design")

## Grafana

As you can see in the image above, Grafana along with MapR-DB JSON Plugin can be used to visualize events, stored at 
MapR-DB JSON Table. Also, its possible to use OpenTSDB Datasource in case when OpenTSDB Sink used.

## Streams design

System design image above depicts as events produced by separate home(the instance of event-generator) published to MapR 
Stream. A stream is a collection of topics that you can manage together by:     
1. Setting security policies that apply to all topics in that stream
2. Setting a default number of partitions for each new topic that is created in the stream
3. Set a time-to-live for messages in every topic in the stream

MapR Smart Home System uses single `/apps/smart-home-stream` stream with `notifications` and `events-*` topics.
Each Home has its own events topic in `events-{home id}` format. Such design allows processing data in a more 
efficient way. [Stream](https://spark.apache.org/docs/2.2.0/streaming-programming-guide.html#discretized-streams-dstreams) 
will consist of a number of RDDs which corresponds to the number of topics, each of RDD will consist of a number of 
partitions that corresponds to topic's partitions. All that guarantees an adequate degree of parallelism.

## What's next?

[Setting up your environment](004-setting-up-your-environment.md) document explains how to prepare MapR Cluster for 
running MapR Smart Home Event Processing System.

## Tutorial links

* [MapR Smart Home](001-introduction.md)
* [Motivation](002-motivation.md)
* [System Design](003-system-design.md)
* [Setting up your environment](004-setting-up-your-environment.md)
* [System deployment](005-system-deployment.md)
* [Grafana for event visualization](006-grafana-for-event-visualization.md)
* [Dockerization](007-dockerization.md)
