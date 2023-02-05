package no.liflig.ks2kurs.bff

import no.liflig.ks2kurs.common.utils.createTestApp
import no.liflig.ks2kurs.common.utils.useSerializer
import no.liflig.ks2kurs.services.car.domain.CarType
import no.liflig.ks2kurs.services.car.dtos.CarDto
import no.liflig.ks2kurs.services.car.dtos.CreateOrEditCarRequest
import no.liflig.ks2kurs.services.person.dtos.CreateOrEditPersonRequest
import no.liflig.ks2kurs.services.person.dtos.PersonDto
import org.http4k.core.Status
import org.http4k.routing.RoutingHttpHandler
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CreateCarAndDriverTest {

  fun createBasics(httpHandler: RoutingHttpHandler): Pair<CarDto, PersonDto> {
    val firstCar = createCar(
      httpHandler = httpHandler,
      createRequest = CreateOrEditCarRequest(
        regNr = "DR94054",
        passengerCapacity = 5,
        carType = CarType.Person,
      ),
    )

    Assertions.assertEquals(Status.OK, firstCar.status)

    val driver = createPerson(
      httpHandler = httpHandler,
      createRequest = CreateOrEditPersonRequest(
        birthDay = LocalDate.parse("2000-01-01"),
        hasLicense = false,
        name = "Ola Normann",
      ),
    )

    Assertions.assertEquals(Status.OK, driver.status)

    return Pair(
      firstCar.useSerializer(CarDto.serializer()),
      driver.useSerializer(PersonDto.serializer()),
    )
  }

  @Test
  fun `Create car and driver test`() {
    val httpHandler = createTestApp()

    Assertions.assertEquals(0, listCars(httpHandler = httpHandler).items.size)
    Assertions.assertEquals(0, listPersons(httpHandler = httpHandler).items.size)

    val (car, driver) = createBasics(httpHandler = httpHandler)

    Assertions.assertEquals(4, car.passengerCapacity)

    val updatedListOfCars = listCars(httpHandler = httpHandler)

    Assertions.assertEquals(1, updatedListOfCars.items.size)
    Assertions.assertEquals(1, listPersons(httpHandler = httpHandler).items.size)

    Assertions.assertEquals(0, updatedListOfCars.items[0].passengers.size)
  }
}
