# Dockerization

## Contents

* [Overview](#overview)
* [Building MapR Smart Home image](#building-mapr-smart-home-image)
* [Running a container](#running-a-container)
* [Tutorial links](#tutorial-links)

## Overview
This document shows how to build MapR Smart Home image and run container from it.
You can easily deploy and test MapR Smart Home system by using Docker. MapR Smart Home image based on 
[MapR Persistent Application Client Container image](https://docstage.mapr.com/60/AdvancedInstallation/UsingtheMapRPACC.html),
which is a Docker-based container image that includes a container-optimized MapR client. The PACC provides seamless 
access to MapR Converged Data Platform services, including MapR-FS, MapR-DB, and MapR-ES. The PACC makes it fast and 
easy to run containerized applications that access data in MapR.

## Building MapR Smart Home image

MapR Smart Home Dockerfile allows you to build MapR Smart Home Docker image, which contains all required components for 
running the app. 

In order to build MapR Smart Home Docker image execute the following commands:
```
$ sbt clean
$ docker build -t mapr-smart-home .
```

Dockerfile contains instructions, which will build the project from sources.

## Running a container

In order to create and run container from existing `mapr-smart-home` image use the following 
command: (adapt the host and cluster name to your environment)
```
$ docker run -it --net=host -e MAPR_CONTAINER_USER=mapr -e MAPR_CONTAINER_GROUP=mapr -e MAPR_CONTAINER_UID=5000 -e MAPR_CONTAINER_GID=5000 -e MAPR_CLDB_HOSTS=cldbnode -e MAPR_CLUSTER='my.cluster.com' -e TSDB_NODE=tsdbnode mapr-smart-home
```

Where:
* `MAPR_CONTAINER_USER`
The user that the user application inside the Docker container will run as. This configuration is functionally 
equivalent to the Docker native `-u` or `--user`. Do not use Docker `-u` or `--user`, as the container needs to start as 
the root user to bring up FUSE before switching to the `MAPR_CONTAINER_USER`.

* `MAPR_CONTAINER_GROUP`
The group that the application inside the Docker container will run as. This is a companion to the 
`MAPR_CONTAINER_USER` option. If a group name is not provided, the default is users. Providing a group name is strongly 
recommended.

* `MAPR_CONTAINER_UID`
The UID that the application inside the Docker container will run as. This is a companion to the MAPR_CONTAINER_USER 
option. If a UID is not provided, the default is UID 1000. Providing a UID is strongly recommended.

* `MAPR_CONTAINER_GID`
The GID that the application inside the Docker container will run as. This is a companion to the MAPR_CONTAINER_USER 
option. If a GID is not provided, the default is GID 1000. Providing a GID is strongly recommended.

* `MAPR_CLDB_HOSTS`
The list of CLDB hosts of your MapR cluster.

* `MAPR_CLUSTER`
The name of the cluster.

* `TSDB_NODE`
Hostname of OpenTSDB node.

In the example above, containers Angular app port `4200` is bound to the hosts `4200` port, so navigate to 
[http://localhost:4200](http://localhost:4200) in order to access MapR Music UI.

## Tutorial links

* [MapR Smart Home](001-introduction.md)
* [Motivation](002-motivation.md)
* [System Design](003-system-design.md)
* [Setting up your environment](004-setting-up-your-environment.md)
* [System deployment](005-system-deployment.md)
* [Grafana for event visualization](006-grafana-for-event-visualization.md)
* [Dockerization](007-dockerization.md)
