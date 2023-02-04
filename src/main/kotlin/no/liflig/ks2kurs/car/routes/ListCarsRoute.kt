package no.liflig.ks2kurs.car.routes

import no.liflig.ks2kurs.car.domain.Car
import no.liflig.ks2kurs.car.dtos.CarsDto
import no.liflig.ks2kurs.car.dtos.toDto
import no.liflig.ks2kurs.common.http4k.Route
import org.http4k.contract.RouteMetaDsl
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with

class ListCarsRoute : Route {
  override fun meta(): RouteMetaDsl.() -> Unit = {
    summary = "list all cars"
    returning(Status.OK, CarsDto.bodyLens to CarsDto.example)
  }

  override fun handler(): HttpHandler = {

    val cars = emptyList<Car>()

    Response(Status.OK).with(CarsDto.bodyLens of CarsDto(cars.map { it.toDto() }))
  }

}
