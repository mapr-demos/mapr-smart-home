# Setting up your environment

## Contents

* [Overview](#overview)
* [Install MapR](#install-mapr)
* [Create Tables and Stream](#create-tables-and-stream)
* [Installing OpenTSDB](#installing-opentsdb)
* [Installing and configuring MapR Client](#installing-and-configuring-mapr-client)
* [What's next?](#whats-next)
* [Tutorial links](#tutorial-links)

## Overview
This document describes the architecture of MapR Smart Home Event Processing System.

## Install MapR

To deploy MapR-Music application you must have a MapR 6.0 cluster running, you have various options:

* [Use MapR Container for Developer](https://maprdocs.mapr.com/home/MapRContainerDevelopers/MapRContainerDevelopersOverview.html)
* [Use a MapR Sandbox VM](https://maprdocs.mapr.com/home/SandboxHadoop/c_sandbox_overview.html)
* [Install a new cluster](https://maprdocs.mapr.com/home/install.html) 

Once you have a running cluster, you can start to create the various tables and import data.

## Create Tables and Stream

You can create Tables and Topics manually or use 
[init-cluster.sh](https://github.com/mapr-demos/mapr-smart-home/blob/devel/bin/init-cluster.sh) script for this purpose.

### Manual setup


1. Create MapR-DB JSON Tables, which will be used by MapR Smart Home Event Processing System:
```
$ maprcli table create -path /apps/homes -tabletype json
$ maprcli table create -path /apps/sensors -tabletype json
$ maprcli table create -path /apps/events -tabletype json
```

2. Change table permissions to allow access for the MapR Smart Home Event Processing System:
```
$ maprcli table cf edit -path /apps/homes -cfname default -readperm p -writeperm p -traverseperm  p
$ maprcli table cf edit -path /apps/sensors -cfname default -readperm p -writeperm p -traverseperm  p
$ maprcli table cf edit -path /apps/events -cfname default -readperm p -writeperm p -traverseperm  p
```

3. Create Stream and topics:
```
$ maprcli stream create -path /apps/smart-home-stream -ischangelog true -consumeperm p
$ maprcli stream topic create -path /apps/smart-home-stream -topic events
$ maprcli stream topic create -path /apps/smart-home-stream -topic notifications
```

4. Change Stream permissions to allow access for the MapR Smart Home Event Processing System:
```
maprcli stream edit -path /apps/smart-home-stream -produceperm p -consumeperm p -topicperm p -copyperm p -adminperm p
```

### Using initialization script

[init-cluster.sh](https://github.com/mapr-demos/mapr-smart-home/blob/devel/bin/init-cluster.sh) script allows you te 
create/recreate required MapR-DB JSON Tables and Topics: 
```
Usage: init-cluster.sh [-r|--recreate] [-h|--help]
Options:
    --recreate  When specified, MapR-DB JSON Tables and Stream will be recreated.
    --help      Prints usage information.
```

### Initialize with test data

Once again you have two options while initializing JSON Tables with test data: manual approach and 
[init-homes.sh](https://github.com/mapr-demos/mapr-smart-home/blob/devel/bin/init-homes.sh) script. Lets consider how to 
initialize data via `mapr dbshell` utility:
```
$ mapr dbshell

insert /apps/homes --value '{"_id":"553d1f54-8956-4452-9e98-b7981fa133da", "name": "Smart Home 1", "address":"Fake street, 1"}'

insert /apps/sensors --value '{"_id":"501c21a6-829a-4b1a-84f1-2d2f3c161fe1","home_id":"553d1f54-8956-4452-9e98-b7981fa133da","name":"Boiler temperature sensor","conversions":["metrics.temperature > 50"]}'

insert /apps/sensors --value '{"_id":"586a6d46-e891-4aeb-8f94-d4efac836df2","home_id":"553d1f54-8956-4452-9e98-b7981fa133da","name":"Complex multi-metric sensor","conversions":["metrics.brightness > 95", "metrics.speed > 250"]}'

```

## Installing OpenTSDB

Follow instructions from 
[Installing OpenTSDB with a Package](https://maprdocs.mapr.com/60/AsyncHBase/AsyncHBase_UsingOpenTSDB_InstallPackage.html) 
page in order to install and run OpenTSDB. Event OpenTSDB Sink uses `smart.home.event` as metric name, so we need to 
create it at OpenTSDB:
```
tsdb mkmetric smart.home.event
```

## Installing and configuring MapR Client

> Note that if you are using MapR Container for Developers, the MapR Client is automatically installed and configure you do not need to do it again.

The `mapr-client` package must be installed on each node where you will be building and running your applications. 
This package installs all of the MapR Libraries needed for application development regardless of programming language or 
type of MapR-DB table (binary or JSON).

Follow the instructions of MapR Documentation:

* [Installing MapR Client](https://maprdocs.mapr.com/home/AdvancedInstallation/SettingUptheClient-install-mapr-client.html)


## What's next?

The [System deployment](005-system-deployment.md) document covers building and running components of MapR Smart Home 
Event Processing System.

## Tutorial links

* [MapR Smart Home](001-introduction.md)
* [Motivation](002-motivation.md)
* [System Design](003-system-design.md)
* [Setting up your environment](004-setting-up-your-environment.md)
* [System deployment](005-system-deployment.md)
* [Grafana for event visualization](006-grafana-for-event-visualization.md)
* [Dockerization](007-dockerization.md)
