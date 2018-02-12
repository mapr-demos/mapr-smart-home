package com.mapr.demos.domain

import scala.beans.BeanProperty

/**
  * Represent's smart home, which is used as source of events.
  */
class Home {

  @BeanProperty var name = ""
  @BeanProperty var sensors = new java.util.ArrayList[Sensor]()

  override def toString: String = s"Home: [name: $name, sensors: $sensors]"
}

class Sensor {

  @BeanProperty var name = ""
  @BeanProperty var metrics = new java.util.ArrayList[Metric]()

  override def toString: String = s"Sensor: [name: $name, metrics: $metrics]"
}

class Metric {

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

  @BeanProperty var from: Double = _
  @BeanProperty var to: Double = _

  override def toString: String = s"RandomRange: [from: $from, to: $to]"
}

class IncrementalRange {

  @BeanProperty var from: Double = _
  @BeanProperty var to: Double = _
  @BeanProperty var step: Double = _

  override def toString: String = s"IncrementalRange: [from: $from, to: $to, step: $step]"
}
