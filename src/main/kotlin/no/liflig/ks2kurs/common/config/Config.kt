package no.liflig.ks2kurs.common.config

import no.liflig.ks2kurs.common.health.getHealthBuildInfo
import no.liflig.properties.intRequired
import no.liflig.properties.loadProperties

object Config {

  val properties = loadProperties()

  // Change these
  val applicationName = "liflig-rest-baseline"

  val corsPolicy = CorsConfig.from(properties)
  val serverPort = properties.intRequired("server.port")
  val buildInfo = properties.getHealthBuildInfo()

  val database = DbConfig.create(properties)
}
