package no.liflig.ks2kurs.common.utils

import no.liflig.ks2kurs.common.health.HealthBuildInfo
import no.liflig.ks2kurs.common.health.HealthService
import no.liflig.ks2kurs.common.http4k.UserPrincipal
import no.liflig.ks2kurs.createApp
import org.http4k.core.RequestContexts
import org.http4k.lens.RequestContextKey
import org.http4k.routing.RoutingHttpHandler
import java.time.Instant


fun createTestApp(): RoutingHttpHandler {
  val contexts = RequestContexts()
  return createApp(
    logHandler = {},
    requestContexts = contexts,
    userPrincipalLens = RequestContextKey.optional<UserPrincipal?>(contexts),
    healthService = HealthService(
      name = "Test-app",
      buildInfo = HealthBuildInfo(
        timestamp = Instant.now(),
        commit = "Initial commit",
        branch = "test",
        number = 123,
      ),
    ),
  )
}
