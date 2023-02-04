package no.liflig.ks2kurs

import mu.KotlinLogging
import no.liflig.ks2kurs.common.auth.AuthService
import no.liflig.ks2kurs.common.auth.DummyAuthService
import no.liflig.ks2kurs.common.auth.UserPrincipalLens
import no.liflig.ks2kurs.common.config.Config
import no.liflig.ks2kurs.common.health.HealthService
import no.liflig.ks2kurs.common.health.createHealthService
import no.liflig.ks2kurs.common.http4k.ServiceRouter
import no.liflig.ks2kurs.common.http4k.UserPrincipal
import no.liflig.ks2kurs.common.http4k.UserPrincipalLog
import no.liflig.ks2kurs.common.http4k.toLog
import no.liflig.logging.RequestResponseLog
import no.liflig.logging.http4k.LoggingFilter
import org.http4k.core.RequestContexts
import org.http4k.lens.RequestContextKey
import org.http4k.server.Jetty
import org.http4k.server.asServer

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
  logger.info { "BuildInfo: ${Config.buildInfo}" }

  val healthService = createHealthService(Config.applicationName, Config.buildInfo)

  val authService: AuthService = DummyAuthService

  val logHandler = LoggingFilter.createLogHandler(
    printStacktraceToConsole = true,
    principalLogSerializer = UserPrincipalLog.serializer(),
  )

  val contexts = RequestContexts()

  val userPrincipalLens = RequestContextKey.optional<UserPrincipal?>(contexts)

  createApp(
    logHandler = logHandler,
    healthService = healthService,
    requestContexts = contexts,
    userPrincipalLens = userPrincipalLens,
  )
    .asServer(Jetty(Config.serverPort))
    .start().also {
      logger.info { "Application started on port ${Config.serverPort}" }
    }
}

fun createApp(
  logHandler: (RequestResponseLog<UserPrincipalLog>) -> Unit,
  userPrincipalLens: UserPrincipalLens,
  healthService: HealthService,
  requestContexts: RequestContexts,
) = ServiceRouter(
  logHandler = logHandler,
  userPrincipalLens = userPrincipalLens,
  principalToLog = UserPrincipal::toLog,
  corsPolicy = Config.corsPolicy.asPolicy(),
  contexts = requestContexts,
  healthService = healthService,
)
  .routingHandler {
    routes += api(errorResponseRenderer)
  }
