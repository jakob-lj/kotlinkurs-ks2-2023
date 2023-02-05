package no.liflig.ks2kurs.bff

import no.liflig.ks2kurs.common.utils.useSerializer
import no.liflig.ks2kurs.services.car.dtos.CarsDto
import no.liflig.ks2kurs.services.car.dtos.CreateOrEditCarRequest
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

fun listPersons(
  httpHandler: RoutingHttpHandler,
): PersonsDto {
  val response = httpHandler(
    Request(Method.GET, "/api/person"),
  )

  Assertions.assertEquals(Status.OK, response.status)

  return response.useSerializer(PersonsDto.serializer())
}
