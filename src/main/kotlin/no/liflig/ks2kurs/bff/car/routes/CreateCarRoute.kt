package no.liflig.ks2kurs.bff.car.routes

import no.liflig.ks2kurs.common.http4k.Route
import no.liflig.ks2kurs.common.http4k.lenses.createBodyLens
import no.liflig.ks2kurs.services.car.domain.Car
import no.liflig.ks2kurs.services.car.domain.CarId
import no.liflig.ks2kurs.services.car.dtos.CarDto
import no.liflig.ks2kurs.services.car.dtos.toDto
import org.http4k.contract.RouteMetaDsl
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with

class CreateCarRoute : Route {
  override fun meta(): RouteMetaDsl.() -> Unit = {
    summary = "Create car"
    description = "Some more description;)"
    returning(Status.OK, CarDto.bodyLens to CarDto.example)
  }

  override fun handler(): HttpHandler = { req ->

    val request = CreateCarRequest.bodyLens(req)

    val car = Car(
      id = CarId(),
      regNr = request.regNr,
    )

    Response(Status.OK).with(CarDto.bodyLens of car.toDto())
  }

  @kotlinx.serialization.Serializable
  data class CreateCarRequest(
    val regNr: String,
  ) {
    companion object {
      val bodyLens by lazy { createBodyLens(serializer()) }
      val example = CreateCarRequest(
        regNr = "DR94054",
      )
    }
  }
}
