package no.liflig.ks2kurs

import mu.KotlinLogging
import no.liflig.documentstore.dao.CrudDaoJdbi
import no.liflig.ks2kurs.common.auth.AuthService
import no.liflig.ks2kurs.common.auth.DummyAuthService
import no.liflig.ks2kurs.common.auth.UserPrincipalLens
import no.liflig.ks2kurs.common.config.Config
import no.liflig.ks2kurs.common.db.DatabaseConfigurator
import no.liflig.ks2kurs.common.domain.ServiceRegistry
import no.liflig.ks2kurs.common.health.HealthService
import no.liflig.ks2kurs.common.health.createHealthService
import no.liflig.ks2kurs.common.http4k.ServiceRouter
import no.liflig.ks2kurs.common.http4k.UserPrincipal
import no.liflig.ks2kurs.common.http4k.UserPrincipalLog
import no.liflig.ks2kurs.common.http4k.toLog
import no.liflig.ks2kurs.services.car.CarService
import no.liflig.ks2kurs.services.car.domain.CarRepositoryJdbi
import no.liflig.ks2kurs.services.car.domain.CarSearchRepositoryJdbi
import no.liflig.ks2kurs.services.car.domain.carSerializerAdapter
import no.liflig.logging.RequestResponseLog
import no.liflig.logging.http4k.LoggingFilter
import org.http4k.core.RequestContexts
import org.http4k.lens.RequestContextKey
import org.http4k.server.Jetty
import org.http4k.server.asServer

private val logger = KotlinLogging.logger {}

const val SHOULD_CLEAN_DATABASE_ON_START = true

fun main(args: Array<String>) {
  logger.info { "BuildInfo: ${Config.buildInfo}" }

  val jdbi = DatabaseConfigurator.createJdbiInstanceAndMigrate(
    DatabaseConfigurator.createDataSource(
      Config.database.jdbcUrl,
      Config.database.username,
      Config.database.password,
    ),
    cleanDatabase = SHOULD_CLEAN_DATABASE_ON_START,
  )

  val healthService = createHealthService(Config.applicationName, Config.buildInfo)

  val authService: AuthService = DummyAuthService

  val logHandler = LoggingFilter.createLogHandler(
    printStacktraceToConsole = true,
    principalLogSerializer = UserPrincipalLog.serializer(),
  )

  val contexts = RequestContexts()

  val userPrincipalLens = RequestContextKey.optional<UserPrincipal?>(contexts)

  val carRepository = CarRepositoryJdbi(
    crudDao = CrudDaoJdbi(jdbi, CarRepositoryJdbi.SQL_TABLE_NAME, carSerializerAdapter),
    searchRepo = CarSearchRepositoryJdbi(jdbi),
  )

  val carService = CarService(
    carRepository = carRepository,
  )

  val serviceRegistry = ServiceRegistry(
    carRepository = carRepository,
    carService = carService,
  )

  createApp(
    logHandler = logHandler,
    healthService = healthService,
    requestContexts = contexts,
    userPrincipalLens = userPrincipalLens,
    serviceRegistry = serviceRegistry,
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
  serviceRegistry: ServiceRegistry,
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
