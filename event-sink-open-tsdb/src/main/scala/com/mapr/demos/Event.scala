package com.mapr.demos

case class Event(_id: Option[String], timestamp: Long, homeId: String, sensorId: String, metrics: Map[String, String])
