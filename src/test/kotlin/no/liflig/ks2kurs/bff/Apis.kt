package no.liflig.ks2kurs.bff

import no.liflig.ks2kurs.bff.car.routes.dtos.AddDriverOrPassengerRequest
import no.liflig.ks2kurs.common.utils.useSerializer
import no.liflig.ks2kurs.services.car.domain.CarId
import no.liflig.ks2kurs.services.car.dtos.CarsDto
import no.liflig.ks2kurs.services.car.dtos.CreateOrEditCarRequest
import no.liflig.ks2kurs.services.person.domain.PersonId
import no.liflig.ks2kurs.services.person.dtos.CreateOrEditPersonRequest
import no.liflig.ks2kurs.services.person.dtos.PersonsDto
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.routing.RoutingHttpHandler
import org.junit.jupiter.api.Assertions

fun createCar(
  httpHandler: RoutingHttpHandler,
  createRequest: CreateOrEditCarRequest,
) = httpHandler(
  Request(Method.POST, "/api/car")
    .with(CreateOrEditCarRequest.bodyLens of createRequest),
)

fun editCar(
  httpHandler: RoutingHttpHandler,
  carId: CarId,
  editRequest: CreateOrEditCarRequest,
) = httpHandler(
  Request(Method.PUT, "/api/car/$carId").with(CreateOrEditCarRequest.bodyLens of editRequest),
)

fun listCars(
  httpHandler: RoutingHttpHandler,
): CarsDto {
  val response = httpHandler(
    Request(Method.GET, "/api/car"),
  )

  Assertions.assertEquals(Status.OK, response.status)

  return response.useSerializer(CarsDto.serializer())
}

fun createPerson(
  httpHandler: RoutingHttpHandler,
  createRequest: CreateOrEditPersonRequest,
) = httpHandler(
  Request(Method.POST, "/api/person")
    .with(CreateOrEditPersonRequest.bodyLens of createRequest),
)

fun editPerson(
  httpHandler: RoutingHttpHandler,
  personId: PersonId,
  createRequest: CreateOrEditPersonRequest,
) = httpHandler(
  Request(Method.PUT, "/api/person/$personId")
    .with(CreateOrEditPersonRequest.bodyLens of createRequest),
)

fun listPersons(
  httpHandler: RoutingHttpHandler,
): PersonsDto {
  val response = httpHandler(
    Request(Method.GET, "/api/person"),
  )

  Assertions.assertEquals(Status.OK, response.status)

  return response.useSerializer(PersonsDto.serializer())
}

fun addDriver(
  httpHandler: RoutingHttpHandler,
  carId: CarId,
  personId: PersonId,
) = httpHandler(
  Request(
    Method.POST,
    "/api/car/$carId/driver",
  ).with(
    AddDriverOrPassengerRequest.bodyLens of AddDriverOrPassengerRequest(
      personId = personId,
    ),
  ),
)

fun removeDriver(
  httpHandler: RoutingHttpHandler,
  carId: CarId,
  personId: PersonId,
) = httpHandler(
  Request(
    Method.DELETE,
    "/api/car/$carId/driver/$personId",
  ),
)

fun addPassenger(
  httpHandler: RoutingHttpHandler,
  carId: CarId,
  personId: PersonId,
) = httpHandler(
  Request(
    Method.POST,
    "/api/car/$carId/passenger",
  ).with(
    AddDriverOrPassengerRequest.bodyLens of AddDriverOrPassengerRequest(
      personId = personId,
    ),
  ),
)

fun removePassenger(
  httpHandler: RoutingHttpHandler,
  carId: CarId,
  personId: PersonId,
) = httpHandler(
  Request(
    Method.DELETE,
    "/api/car/$carId/passenger/$personId",
  ),
)
