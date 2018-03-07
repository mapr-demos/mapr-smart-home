package com.mapr.demos.domain

case class Event(timestamp: Long, homeId: String, sensorId: String, metrics: Map[String, String])
