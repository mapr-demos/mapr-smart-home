package com.mapr.demos.util

import java.io.InputStream

import com.mapr.demos.domain.Home
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

/**
  * Parses smart home descriptor file into instances of Home class, which represent's smart home and used as source of
  * events.
  */
object HomeDescriptorParser {

  def fromResources(resourceName: String): Home = {

    val input = getClass.getResourceAsStream(resourceName)
    val home = parseYaml(input)

    assertHomeIsValid(home)

    home
  }

  private def parseYaml(input: InputStream): Home = {

    val yaml = new Yaml(new Constructor(classOf[Home]))
    yaml.load(input).asInstanceOf[Home]
  }

  private def assertHomeIsValid(home: Home): Unit = {

    require(home != null, "Home can not be null")

    home.sensors.forEach(s =>
      s.metrics.forEach(m => {
        require(m.intervalMs > 0, s"Metric producing interval must be greater than zero, but got: $m")
        require(m.strategy != null, s"Metric strategy is required for $m")
        require(m.strategy.defined, s"Metric strategy is required for $m")
        require(m.strategy.single, s"Only one strategy per metric allowed. Invalid metric: $m")
      }))
  }

}
