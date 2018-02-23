package com.mapr.demos.domain

import scala.beans.BeanProperty

/**
  * Represent's smart home, which is used as source of events.
  */
class HomeDescriptor {

  @BeanProperty var id = ""
  @BeanProperty var name = ""
  @BeanProperty var sensors = new java.util.ArrayList[SensorDescriptor]()

  override def toString: String = s"Home: [id: $id, name: $name, sensors: $sensors]"
}

class SensorDescriptor {

  @BeanProperty var id = ""
  @BeanProperty var name = ""
  @BeanProperty var metrics = new java.util.ArrayList[MetricDescriptor]()

  override def toString: String = s"Sensor: [id: $id, name: $name, metrics: $metrics]"
}

class MetricDescriptor {

  @BeanProperty var name = ""
  @BeanProperty var intervalMs : Long = _
  @BeanProperty var strategy: Strategy = _

  override def toString: String = s"Metric: [name: $name, intervalMs: $intervalMs, strategy: $strategy]"
}

class Strategy {

  @BeanProperty var randomRange: RandomRange = _
  @BeanProperty var incrementalRange: IncrementalRange = _

  def defined : Boolean = {
    randomRange != null || incrementalRange != null
  }

  def single : Boolean = {
    defined && (randomRange == null || incrementalRange == null)
  }

  override def toString: String = s"Strategy: [randomRange: $randomRange, incrementalRange: $incrementalRange]"
}

class RandomRange {

  @BeanProperty var from: Int = _
  @BeanProperty var to: Int = _

  override def toString: String = s"RandomRange: [from: $from, to: $to]"
}

class IncrementalRange {

  @BeanProperty var from: Double = _
  @BeanProperty var to: Double = _
  @BeanProperty var step: Double = _

  override def toString: String = s"IncrementalRange: [from: $from, to: $to, step: $step]"
}
