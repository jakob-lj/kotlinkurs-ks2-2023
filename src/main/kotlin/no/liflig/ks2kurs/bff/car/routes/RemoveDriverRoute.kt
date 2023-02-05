package no.liflig.ks2kurs.bff.car.routes

import kotlinx.coroutines.runBlocking
import no.liflig.ks2kurs.common.domain.ServiceRegistry
import no.liflig.ks2kurs.common.http4k.Route
import no.liflig.ks2kurs.common.http4k.errors.PersonError
import no.liflig.ks2kurs.common.http4k.errors.PersonErrorCode
import no.liflig.ks2kurs.services.car.domain.CarId
import no.liflig.ks2kurs.services.car.dtos.CarDto
import no.liflig.ks2kurs.services.car.dtos.toDto
import no.liflig.ks2kurs.services.person.domain.PersonId
import org.http4k.contract.RouteMetaDsl
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with

class RemoveDriverRoute(override val sr: ServiceRegistry) : Route {
  override fun meta(): RouteMetaDsl.() -> Unit = {
    summary = "remove driver"
    returning(Status.OK, CarDto.bodyLens to CarDto.example)
  }

  override fun handler(vararg params: String): HttpHandler = {
    val carId = CarId(params.first())
    val personId = PersonId(params[1])

    runBlocking {
      val person = sr.personRepository.get(personId) ?: throw PersonError(PersonErrorCode.PersonNotFound)

      val updatedCar = sr.carService.removeDriver(personId, carId)

      Response(Status.OK).with(CarDto.bodyLens of updatedCar.toDto())
    }
  }
}
