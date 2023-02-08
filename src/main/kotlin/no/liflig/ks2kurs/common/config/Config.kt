package no.liflig.ks2kurs.common.config

import no.liflig.ks2kurs.common.health.getHealthBuildInfo
import org.http4k.core.Method

object Config {

  // val properties = loadProperties()

  // Change these
  val applicationName = "liflig-rest-baseline"

  val corsPolicy = CorsConfig(
    allowedHeaders = listOf("*"),
    allowedMethods = listOf(
      Method.GET,
      Method.POST,
      Method.DELETE,
      Method.OPTIONS,
      Method.PUT,
      Method.PATCH,
      Method.HEAD,
    ),
    allowedOrigins = listOf("http://localhost:3000"),
  )
  val serverPort = 8080
  val buildInfo = getHealthBuildInfo()
}
