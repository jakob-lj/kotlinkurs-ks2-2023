package no.liflig.ks2kurs

import mu.KotlinLogging
import no.liflig.ks2kurs.common.auth.AuthService
import no.liflig.ks2kurs.common.auth.DummyAuthService
import no.liflig.ks2kurs.common.auth.UserPrincipalLens
import no.liflig.ks2kurs.common.config.Config
import no.liflig.ks2kurs.common.db.CrudDao
import no.liflig.ks2kurs.common.domain.ServiceRegistry
import no.liflig.ks2kurs.common.health.HealthService
import no.liflig.ks2kurs.common.health.createHealthService
import no.liflig.ks2kurs.common.http4k.ServiceRouter
import no.liflig.ks2kurs.common.http4k.UserPrincipal
import no.liflig.ks2kurs.services.car.CarService
import no.liflig.ks2kurs.services.car.domain.Car
import no.liflig.ks2kurs.services.car.domain.CarId
import no.liflig.ks2kurs.services.person.PersonService
import no.liflig.ks2kurs.services.person.domain.Person
import no.liflig.ks2kurs.services.person.domain.PersonId
import org.http4k.core.RequestContexts
import org.http4k.lens.RequestContextKey
import org.http4k.server.Jetty
import org.http4k.server.asServer

private val logger = KotlinLogging.logger {}

const val SHOULD_CLEAN_DATABASE_ON_START = true

fun main(args: Array<String>) {
  logger.info { "BuildInfo: ${Config.buildInfo}" }

  val healthService = createHealthService(Config.applicationName, Config.buildInfo)

  val authService: AuthService = DummyAuthService

  val contexts = RequestContexts()

  val userPrincipalLens = RequestContextKey.optional<UserPrincipal?>(contexts)

  val carRepository = CrudDao<Car, CarId>()

  val carService = CarService(
    carRepository = carRepository,
  )

  val personRepository = CrudDao<Person, PersonId>()

  val personService = PersonService(
    personRepository = personRepository,
  )

  val serviceRegistry = ServiceRegistry(
    carRepository = carRepository,
    carService = carService,
    personRepository = personRepository,
    personService = personService,
  )

  createApp(
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
  userPrincipalLens: UserPrincipalLens,
  healthService: HealthService,
  requestContexts: RequestContexts,
  serviceRegistry: ServiceRegistry,
) = ServiceRouter(
  userPrincipalLens = userPrincipalLens,
  corsPolicy = Config.corsPolicy.asPolicy(),
  contexts = requestContexts,
  healthService = healthService,
)
  .routingHandler {
    routes += api(serviceRegistry)
  }
