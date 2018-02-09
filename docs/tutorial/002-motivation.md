# Motivation

## Contents

* [Overview](#overview)
* [Business needs](#business-needs)
* [What's next?](#whats-next)
* [Tutorial links](#tutorial-links)

## Overview

This document explains the motivation of creating MapR Smart Home Event Processing System. We have decided to use smart 
homes as a subject of tutorial instead of creating a general-purpose event-processing system. It will make the tutorial 
easier to adopt since it always easier to operate on concrete things instead of abstract ones. Thus, we will suppose 
that there are stakeholders which are interested in creating system for collecting and monitoring various metrics, 
produced by smart homes.

## Business needs

The system must:
* collect metrics

Various metrics from multiples devices per each home must be collected. There must be the ability to add new home/device easily. 
Possible metrics: temperature, electricity consumption.

* convert metrics using user-defined conversions

There must be the ability for the users to define metrics conversions. Possible conversions: convert Fahrenheit to Celsius, 
convert electricity/water/gas consumption to house maintenance cost.

* persist metrics

Metrics must be persisted in reliable storage.

* provide UI for visualizing metrics and metrics' sources

There must be a UI for system monitoring, which must provide information about active metrics sources and available 
metrics. UI also can be used for defining metrics conversions, system configuration.

## What's next?

The [System Design](003-system-design.md) document describes the architecture of MapR Smart Home Event Processing System.

## Tutorial links

* [MapR Smart Home](001-introduction.md)
* [Motivation](002-motivation.md)
* [System Design](003-system-design.md)

