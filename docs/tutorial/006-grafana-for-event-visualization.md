# Grafana for event visualization

## Contents

* [Overview](#overview)
* [Install Grafana](#install-grafana)
* [MapR-DB JSON Data Source](#mapr-db-json-data-source)
* [OpenTSDB Data Source](#opentsdb-data-source)
* [Smart Homes Grafana Dashboard](#smart-homes-grafana-dashboard)
* [What's next?](#whats-next)
* [Tutorial links](#tutorial-links)

## Overview
This document explains how to visualize events using Grafana and MapR-Db JSON/OpenTSDB as data sources. Also, you will 
find instructions on how to import and use 
[Grafana Smart Homes Dashboard](https://github.com/mapr-demos/mapr-smart-home/blob/devel/grafana-dashboard/smart-homes-dashboard.json).

![](../images/grafana-smart-homes-dashboard.png?raw=true "Grafana Smart Homes Dashboard")

## Install Grafana

You can install Grafana on dev machine or on one of the cluster nodes. If you are installing Grafana on cluster node, 
use the following command:
```
$ sudo yum install mapr-grafana
```

In case of installing Grafana on dev machine, you can install Grafana from 
[Grafana Labs](https://grafana.com/grafana/download/4.4.2). Plugin tested against Grafana 4.4.2 and it's possible that 
plugin is incompatible with other versions of Grafana, so for now ensure that version of Grafana is `4.4.2`:
For Ubuntu/Debian:
```
wget https://s3-us-west-2.amazonaws.com/grafana-releases/release/grafana_4.4.2_amd64.deb 
sudo dpkg -i grafana_4.4.2_amd64.deb 
```

For Redhat/Centos:
```
wget https://s3-us-west-2.amazonaws.com/grafana-releases/release/grafana-4.4.2-1.x86_64.rpm 
sudo yum localinstall grafana-4.4.2-1.x86_64.rpm 
```



## MapR-DB JSON Data Source

Grafana along with [MapR-DB JSON Datasoutce Plugin](https://github.com/mapr-demos/mapr-db-json-grafana-plugin) is 
intended to visualize arbitrary data stored at MapR-DB JSON tables.

It allows users to:
* create Graph and Table panels 
* visualize raw JSON documents
* specify condition using [MapR-DB Shell](https://maprdocs.mapr.com/60/ReferenceGuide/mapr_dbshell.html) condition syntax 
* specify projection
* use various metric aggregations: Document count, Field value, Field max, Field min, Field avg

Follow 
[Plugin installation instructions](https://github.com/mapr-demos/mapr-db-json-grafana-plugin/blob/master/doc/002-installation.md)
in order to setup Grafana for visualizing events stored at MapR-DB JSON Tables.

## OpenTSDB Data Source

Grafana ships with advanced support for OpenTSDB. 
[Using OpenTSDB in Grafana](http://docs.grafana.org/features/datasources/opentsdb/#using-opentsdb-in-grafana) document 
explains how to add OpenTSDB datasource.

## Smart Homes Grafana Dashboard

MapR Smart Home repository contains 
[Grafana Smart Homes Dashboard](https://github.com/mapr-demos/mapr-smart-home/blob/devel/grafana-dashboard/smart-homes-dashboard.json)
which is ready to be imported to your Grafana installation. Please, follow 
[instructions from Grafana Docs](http://docs.grafana.org/reference/export_import/#importing-a-dashboard)
to import the dashboard.

Grafana Smart Homes Dashboard consists of several Graph and Table Panels, which include:

### MaR-DB JSON: All metrics

Graph panel, which visualizes all metrics of single smart home. Uses MapR-DB JSON Datasource. 
![](../images/json-all-metrics.png?raw=true "MaR-DB JSON: All metrics")

### OpenTSDB: All metrics

The same panel as above but uses OpenTSDB as datasource.
![](../images/tsdb-all-metrics.png?raw=true "OpenTSDB: All metrics")

## What's next?

[Dockerization](007-dockerization.md) document shows how to build MapR Smart Home image and run container from it.

## Tutorial links

* [MapR Smart Home](001-introduction.md)
* [Motivation](002-motivation.md)
* [System Design](003-system-design.md)
* [Setting up your environment](004-setting-up-your-environment.md)
* [System deployment](005-system-deployment.md)
* [Grafana for event visualization](006-grafana-for-event-visualization.md)
* [Dockerization](007-dockerization.md)
