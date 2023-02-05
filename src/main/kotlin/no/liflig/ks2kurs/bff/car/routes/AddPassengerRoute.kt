package no.liflig.ks2kurs.bff.car.routes

import kotlinx.coroutines.runBlocking
import no.liflig.ks2kurs.bff.car.routes.dtos.AddDriverOrPassengerRequest
import no.liflig.ks2kurs.common.domain.ServiceRegistry
import no.liflig.ks2kurs.common.http4k.Route
import no.liflig.ks2kurs.common.http4k.errors.CarError
import no.liflig.ks2kurs.common.http4k.errors.CarErrorCode
import no.liflig.ks2kurs.services.car.domain.CarId
import no.liflig.ks2kurs.services.car.dtos.CarDto
import no.liflig.ks2kurs.services.car.dtos.toDto
import org.http4k.contract.RouteMetaDsl
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with

class AddPassengerRoute(override val sr: ServiceRegistry) : Route {
  override fun meta(): RouteMetaDsl.() -> Unit = {
    summary = "add passenger"
    receiving(AddDriverOrPassengerRequest.bodyLens to AddDriverOrPassengerRequest.example)
    returning(Status.OK, CarDto.bodyLens to CarDto.example)
  }

  override fun handler(vararg params: String): HttpHandler = { req ->
    val carId = CarId(params.first())

    val request = AddDriverOrPassengerRequest.bodyLens(req)

    runBlocking {
      val car = sr.carRepository.get(carId) ?: throw CarError(CarErrorCode.CarNotFound)

      val updatedCar = sr.carService.addPassenger(request.personId, car.item.id)

      Response(Status.OK).with(CarDto.bodyLens of updatedCar.toDto())
    }
  }
}
