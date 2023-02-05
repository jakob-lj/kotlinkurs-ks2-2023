package no.liflig.ks2kurs.common.utils

import no.liflig.documentstore.dao.CrudDaoJdbi
import no.liflig.ks2kurs.common.domain.ServiceRegistry
import no.liflig.ks2kurs.common.health.HealthBuildInfo
import no.liflig.ks2kurs.common.health.HealthService
import no.liflig.ks2kurs.common.http4k.UserPrincipal
import no.liflig.ks2kurs.common.http4k.UserPrincipalLog
import no.liflig.ks2kurs.createApp
import no.liflig.ks2kurs.services.car.CarService
import no.liflig.ks2kurs.services.car.domain.CarRepositoryJdbi
import no.liflig.ks2kurs.services.car.domain.CarSearchRepositoryJdbi
import no.liflig.ks2kurs.services.car.domain.carSerializerAdapter
import no.liflig.ks2kurs.services.person.PersonService
import no.liflig.ks2kurs.services.person.domain.PersonRepositoryJdbi
import no.liflig.ks2kurs.services.person.domain.PersonSearchRepositoryJdbi
import no.liflig.ks2kurs.services.person.domain.personSerializationAdapter
import no.liflig.logging.http4k.LoggingFilter
import org.http4k.core.RequestContexts
import org.http4k.lens.RequestContextKey
import org.http4k.routing.RoutingHttpHandler
import java.time.Instant

fun createTestApp(): RoutingHttpHandler {
  val jdbi = createJdbiForTests(useStaticPort = false)

  val contexts = RequestContexts()

  val carRepository = CarRepositoryJdbi(
    crudDao = CrudDaoJdbi(jdbi, CarRepositoryJdbi.SQL_TABLE_NAME, carSerializerAdapter),
    searchRepo = CarSearchRepositoryJdbi(jdbi),
  )

  val carService = CarService(
    carRepository = carRepository,
  )

  val personRepository = PersonRepositoryJdbi(
    crudDao = CrudDaoJdbi(jdbi, PersonRepositoryJdbi.SQL_TABLE_NAME, personSerializationAdapter),
    searchRepo = PersonSearchRepositoryJdbi(jdbi),
  )

  val personService = PersonService(
    personRepository = personRepository,
  )

  val serviceRegistry = ServiceRegistry(
    carRepository = carRepository,
    carService = carService,
    personRepository = personRepository,
    personService = personService,
  )

  return createApp(
    logHandler = LoggingFilter.createLogHandler(
      printStacktraceToConsole = true,
      principalLogSerializer = UserPrincipalLog.serializer(),
    ),
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
    serviceRegistry = serviceRegistry,
  )
}
