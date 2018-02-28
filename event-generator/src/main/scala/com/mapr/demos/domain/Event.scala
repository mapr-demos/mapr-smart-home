package com.mapr.demos.domain

case class Event(homeId: String, sensorId: String, metrics: Map[String, String])
