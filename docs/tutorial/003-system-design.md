# System Design

## Contents

* [Overview](#overview)
* [Design](#design)
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

Receives events and stores them at OpenTSDB.


![](../images/smart-home-design.png?raw=true "System design")

## Grafana

As you can see in the image above, Grafana along with MapR-DB JSON Plugin can be used to visualize events, stored at 
MapR-DB JSON Table. Also, its possible to use OpenTSDB Datasource in case when OpenTSDB Sink used.

## What's next?

## Tutorial links

* [MapR Smart Home](001-introduction.md)
* [Motivation](002-motivation.md)
* [System Design](003-system-design.md)

