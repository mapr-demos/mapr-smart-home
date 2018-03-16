# System deployment

## Contents

* [Overview](#overview)
* [Build the project](#build-the-project)
* [Running system components](#running-system-components)
* [What's next?](#whats-next)
* [Tutorial links](#tutorial-links)

## Overview
This document shows how to build and run components of MapR Smart Home Event Processing System.

## Build the project

1. Clone the Smart Home repository:

```
$ https://github.com/mapr-demos/mapr-smart-home.git
```

2. Build the project using SBT assembly plugin:

```
$ cd mapr-smart-home

$ sbt clean assembly

```

3. Build Angular web client:

```
$ cd web-client/

$ npm install

```

After that, each of sub-projects will contain `target` directory with built `jar` file within it.

Note: Smart Home project contains some integration tests, which require access to MapR Cluster. Therefore project 
should be built on machine with MapR Client installed. Otherwise, you are free to skip tests while assembling:

```
$ sbt "set test in assembly := {}" clean assembly
```

## Running system components

Assuming that current directory is Smart Home Project root you can run all component using following commands.

1. Event Processor
```
$ java -jar event-processor/target/scala-2.11/event-processor-assembly-0.1.0-SNAPSHOT.jar
```

2.  Web application backend 
```
$ java -jar webapp/target/scala-2.12/webapp-assembly-1.0-SNAPSHOT.jar
```

3. Event MapR-DB JSON Sink
```
$ java -jar event-sink-mapr-db-json/target/scala-2.11/event-mapr-db-sink-assembly-0.1.0-SNAPSHOT.jar
```

4. Event OpenTSDB Sink
```
$ java -jar event-sink-open-tsdb/target/scala-2.11/event-open-tsdb-sink-assembly-0.1.0-SNAPSHOT.jar
```
Also, its possible to specify OpenTSDB node hostname as argument:
```
$ java -jar event-sink-open-tsdb/target/scala-2.11/event-open-tsdb-sink-assembly-0.1.0-SNAPSHOT.jar tsdbnode
```

5. Event Generator
```
$ java -jar event-generator/target/scala-2.11/event-generator-assembly-0.1.0-SNAPSHOT.jar
```

6. Web application UI
```
$ cd web-client/ 
$ npm start
```

## What's next?

The [Grafana for event visualization](006-grafana-for-event-visualization.md) document 
explains how to visualize events using MapR-Db JSON and OpenTSDB as data sources.

## Tutorial links

* [MapR Smart Home](001-introduction.md)
* [Motivation](002-motivation.md)
* [System Design](003-system-design.md)
* [Setting up your environment](004-setting-up-your-environment.md)
* [System deployment](005-system-deployment.md)
* [Grafana for event visualization](006-grafana-for-event-visualization.md)
* [Dockerization](007-dockerization.md)
