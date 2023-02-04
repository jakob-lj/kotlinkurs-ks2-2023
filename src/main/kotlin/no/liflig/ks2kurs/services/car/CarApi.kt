package no.liflig.ks2kurs.services.car

import no.liflig.ks2kurs.bff.car.routes.CreateCarRoute
import no.liflig.ks2kurs.bff.car.routes.ListCarsRoute
import no.liflig.ks2kurs.common.http4k.Api
import org.http4k.contract.meta
import org.http4k.core.Method

class CarApi(override val prefix: String) : Api {

  private val listCarsRoute = ListCarsRoute()
  private val createCar = CreateCarRoute()

  override val routes = listOf(

    prefix meta listCarsRoute.meta()
      bindContract Method.GET to listCarsRoute::handler,

    prefix meta createCar.meta()
      bindContract Method.POST to createCar::handler,
  )
}
