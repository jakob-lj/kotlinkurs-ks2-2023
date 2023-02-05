package no.liflig.ks2kurs.bff.car

import no.liflig.ks2kurs.bff.car.routes.AddDriverRoute
import no.liflig.ks2kurs.bff.car.routes.AddPassengerRoute
import no.liflig.ks2kurs.bff.car.routes.CreateCarRoute
import no.liflig.ks2kurs.bff.car.routes.EditCarRoute
import no.liflig.ks2kurs.bff.car.routes.ListCarsRoute
import no.liflig.ks2kurs.bff.car.routes.RemoveDriverRoute
import no.liflig.ks2kurs.bff.car.routes.RemovePassengerRoute
import no.liflig.ks2kurs.common.domain.ServiceRegistry
import no.liflig.ks2kurs.common.http4k.Api
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.Method
import org.http4k.lens.Path

class CarApi(override val prefix: String, override val serviceRegistry: ServiceRegistry) : Api {

  private val listCarsRoute = ListCarsRoute(serviceRegistry)
  private val createCar = CreateCarRoute(serviceRegistry)
  private val editCar = EditCarRoute(serviceRegistry)
  private val addDriver = AddDriverRoute(serviceRegistry)
  private val removeDriver = RemoveDriverRoute(serviceRegistry)
  private val addPassenger = AddPassengerRoute(serviceRegistry)
  private val removePassenger = RemovePassengerRoute(serviceRegistry)

  override val routes = listOf(
    prefix meta listCarsRoute.meta()
      bindContract Method.GET to listCarsRoute.handler(),

    prefix meta createCar.meta()
      bindContract Method.POST to createCar.handler(),

    prefix / Path.of("carId") meta editCar.meta()
      bindContract Method.PUT to { carId -> editCar.handler(carId) },

    prefix / Path.of("carId") / "driver" meta addDriver.meta()
      bindContract Method.POST to { carId, _ -> addDriver.handler(carId) },

    prefix / Path.of("carId") / "driver" / Path.of("driverId") meta removeDriver.meta()
      bindContract Method.DELETE to { carId, _, driverId -> removeDriver.handler(carId, driverId) },

    prefix / Path.of("carId") / "passenger" meta addPassenger.meta()
      bindContract Method.POST to { carId, _ -> addPassenger.handler(carId) },

    prefix / Path.of("carId") / "passenger" / Path.of("passengerId") meta removePassenger.meta()
      bindContract Method.DELETE to { carId, _, passengerId -> removePassenger.handler(carId, passengerId) },
  )
}
