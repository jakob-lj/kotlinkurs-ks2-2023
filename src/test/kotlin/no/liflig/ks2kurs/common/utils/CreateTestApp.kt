package no.liflig.ks2kurs.common.utils

import no.liflig.ks2kurs.common.db.CrudDao
import no.liflig.ks2kurs.common.domain.ServiceRegistry
import no.liflig.ks2kurs.common.health.HealthBuildInfo
import no.liflig.ks2kurs.common.health.HealthService
import no.liflig.ks2kurs.common.http4k.UserPrincipal
import no.liflig.ks2kurs.createApp
import no.liflig.ks2kurs.services.car.CarService
import no.liflig.ks2kurs.services.car.domain.Car
import no.liflig.ks2kurs.services.car.domain.CarId
import no.liflig.ks2kurs.services.person.PersonService
import no.liflig.ks2kurs.services.person.domain.Person
import no.liflig.ks2kurs.services.person.domain.PersonId
import org.http4k.core.RequestContexts
import org.http4k.lens.RequestContextKey
import org.http4k.routing.RoutingHttpHandler
import java.time.Instant

fun createTestApp(): RoutingHttpHandler {
  val contexts = RequestContexts()

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

  return createApp(
    requestContexts = contexts,
    userPrincipalLens = RequestContextKey.optional<UserPrincipal?>(contexts),
    healthService = HealthService(
      name = "Test-app",
      buildInfo = HealthBuildInfo(
        timestamp = Instant.now(),
      ),
    ),
    serviceRegistry = serviceRegistry,
  )
}
