package no.liflig.ks2kurs.common.config

import org.http4k.core.Method
import org.http4k.filter.AllowAll
import org.http4k.filter.AnyOf
import org.http4k.filter.CorsPolicy
import org.http4k.filter.OriginPolicy

data class CorsConfig(
  val allowedOrigins: List<String>,
  val allowedHeaders: List<String>,
  val allowedMethods: List<Method>,
) {
  fun asPolicy(): CorsPolicy =
    CorsPolicy(
      if ("*" in allowedOrigins) OriginPolicy.AllowAll()
      else OriginPolicy.AnyOf(allowedOrigins),
      allowedHeaders,
      allowedMethods,
    )
}
