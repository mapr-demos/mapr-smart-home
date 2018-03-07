package com.mapr.demos.util

import com.mapr.demos.domain.{Event, HomeDescriptor, MetricDescriptor, SensorDescriptor}

object HomeEventsGenerator {

  private val lastMetricValues: scala.collection.mutable.Map[(String, String, String), String] = scala.collection.mutable.Map()
  private val random = new scala.util.Random

  def events(home: HomeDescriptor, millisFromStart: Long): Seq[Event] = {

    require(home != null, "Home can not be null")
    require(millisFromStart >= 0, "Milliseconds from start can not be negative number")

    import collection.JavaConverters._

    home.sensors.asScala.map(sensor => (sensor, sensor.metrics.asScala.filter(metric => millisFromStart % metric.intervalMs == 0)))
      .flatMap {
        case (sensor, sensorMetricsToBeSent) => sensorMetricsToBeSent.map(metric => nextEvent(home, sensor, metric))
      }
      .filter(o => o.isDefined)
      .map(o => o.get)
  }

  private def nextEvent(home: HomeDescriptor, sensor: SensorDescriptor, metric: MetricDescriptor): Option[Event] = {

    if (metric.strategy.randomRange != null) {

      val strategy = metric.strategy.randomRange
      val metricValue = strategy.from + random.nextInt((strategy.to - strategy.from) + 1)

      Some(Event(System.currentTimeMillis(), home.id, sensor.id, Map(metric.name -> metricValue.toString)))
    } else if (metric.strategy.incrementalRange != null) {

      val strategy = metric.strategy.incrementalRange

      val key = (home.id, sensor.id, metric.name)
      val lastValue = if (lastMetricValues.contains(key)) lastMetricValues.get(key) else None

      val newValue = if (lastValue.isDefined) {
        (lastValue.get.toDouble + strategy.step) % strategy.to
      } else {
        strategy.from
      }

      lastMetricValues.put(key, newValue.toString)

      Some(Event(System.currentTimeMillis(), home.id, sensor.id, Map(metric.name -> newValue.toString)))
    } else {
      None
    }

  }

}
