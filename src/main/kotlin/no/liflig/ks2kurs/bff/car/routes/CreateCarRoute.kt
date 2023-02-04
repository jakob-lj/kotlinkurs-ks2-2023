package no.liflig.ks2kurs.bff.car.routes

import kotlinx.coroutines.runBlocking
import no.liflig.ks2kurs.common.domain.ServiceRegistry
import no.liflig.ks2kurs.common.http4k.Route
import no.liflig.ks2kurs.services.car.dtos.CarDto
import no.liflig.ks2kurs.services.car.dtos.CreateOrEditCarRequest
import no.liflig.ks2kurs.services.car.dtos.toDto
import org.http4k.contract.RouteMetaDsl
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with

class CreateCarRoute(override val sr: ServiceRegistry) : Route {
  override fun meta(): RouteMetaDsl.() -> Unit = {
    summary = "Create car"
    description = "Some more description;)"
    returning(Status.OK, CarDto.bodyLens to CarDto.example)
  }

  override fun handler(): HttpHandler = { req ->

    val request = CreateOrEditCarRequest.bodyLens(req)

    val car = runBlocking { sr.carService.create(request) }

    Response(Status.OK).with(CarDto.bodyLens of car.toDto())
  }
}
