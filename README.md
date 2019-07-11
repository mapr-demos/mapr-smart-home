# MapR Smart Home

This repository contains sample event processing system based on MapR Converged Data Platform along with tutuorial which
walks through a process of system development, starting from defining business requirements and ending with system deployment
and testing.

## Security Note

Please note that this demo has not been updated recently and has a known security issue based on a dependency on
`lodash.mergeTo` version 4.6.0. To fix this, you will need to update the `yarn.lock` file using the standard yarn tools 
on a system where a more recent version in your environment. If you do this, please send a pull request with the result.

## Overview

MapR Smart Home Tutorial is designated to walk user through a process of developing event processing system, starting 
from defining business requirements and ending with system deployment and testing. The system is built on top of 
[MapR Converged Data Platform](https://mapr.com/products/mapr-converged-data-platform/) and you will be familiarized with:

* MapR Event Streams

[MapR Streams](https://mapr.com/products/mapr-streams/) is a new distributed messaging system for streaming event data 
at scale, and it’s integrated into the MapR converged platform, which is uses Apache Kafka API. Thus, if you’re already 
familiar with Kafka, you’ll find it particularly easy to get started with MapR Streams.

* Apache Spark

[Spark](https://mapr.com/products/apache-spark/) is a general-purpose engine for large-scale data processing. 
It supports rapid application development for big data and allows for code reuse across batch, interactive, and 
streaming applications. 

* MapR-DB: Database for Global Data-Intensive Applications

[MapR-DB](https://mapr.com/products/mapr-db/) is a high performance NoSQL (“Not Only SQL”) database management system 
built into the MapR Converged Data Platform. It is a highly scalable multi-model database that brings together 
operations and analytics, and real-time streaming and database workloads to enable a broader set of next-generation 
data-intensive applications in organizations.

## Documentation links
* [Tutorial](docs/tutorial/001-introduction.md)
