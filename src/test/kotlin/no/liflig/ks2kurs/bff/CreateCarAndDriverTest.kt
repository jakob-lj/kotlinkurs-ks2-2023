package no.liflig.ks2kurs.bff

import no.liflig.ks2kurs.common.http4k.errors.CarErrorCode
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

@kotlinx.serialization.Serializable
class CarErrorTestDto(val code: CarErrorCode)

class CreateCarAndDriverTest {

  fun createBasics(httpHandler: RoutingHttpHandler): Triple<CarDto, PersonDto, PersonDto> {
    val firstCar = createCar(
      httpHandler = httpHandler,
      createRequest = CreateOrEditCarRequest(
        regNr = "DR94054",
        passengerCapacity = 5,
        carType = CarType.Person,
      ),
    )

    Assertions.assertEquals(Status.OK, firstCar.status)

    val passenger = createPerson(
      httpHandler = httpHandler,
      createRequest = CreateOrEditPersonRequest(
        birthDay = LocalDate.parse("2000-01-01"),
        hasLicense = false,
        name = "Ola Passasjer",
      ),
    )

    val driver = createPerson(
      httpHandler = httpHandler,
      createRequest = CreateOrEditPersonRequest(
        birthDay = LocalDate.parse("1999-01-01"),
        hasLicense = true,
        name = "Ola Sjåfør",
      ),
    )

    Assertions.assertEquals(Status.OK, passenger.status)

    return Triple(
      firstCar.useSerializer(CarDto.serializer()),
      passenger.useSerializer(PersonDto.serializer()),
      driver.useSerializer(PersonDto.serializer()),
    )
  }

  @Test
  fun `Create car and driver test`() {
    val httpHandler = createTestApp()

    Assertions.assertEquals(0, listCars(httpHandler = httpHandler).items.size)
    Assertions.assertEquals(0, listPersons(httpHandler = httpHandler).items.size)

    val (car, passenger, driver) = createBasics(httpHandler = httpHandler)

    Assertions.assertEquals(5, car.passengerCapacity)

    val updatedListOfCars = listCars(httpHandler = httpHandler)

    Assertions.assertEquals(1, updatedListOfCars.items.size)
    Assertions.assertEquals(2, listPersons(httpHandler = httpHandler).items.size)

    Assertions.assertEquals(0, updatedListOfCars.items[0].passengers.size)

    val addDriverResponse = addDriver(
      httpHandler = httpHandler,
      carId = car.id,
      personId = driver.id,
    )

    Assertions.assertEquals(Status.OK, addDriverResponse.status)

    val failedAddDriverResponseDueToMissingLicense = addDriver(
      httpHandler = httpHandler,
      carId = car.id,
      personId = passenger.id,
    )

    Assertions.assertEquals(Status.BAD_REQUEST, failedAddDriverResponseDueToMissingLicense.status)
    Assertions.assertEquals(
      CarErrorCode.PersonDoesNotHaveValidCertificate,
      failedAddDriverResponseDueToMissingLicense.useSerializer(CarErrorTestDto.serializer()).code,
    )

    val failedAddDriverResponseDueToDuplicate = addDriver(
      httpHandler = httpHandler,
      carId = car.id,
      personId = driver.id,
    )

    Assertions.assertEquals(Status.BAD_REQUEST, failedAddDriverResponseDueToDuplicate.status)
    Assertions.assertEquals(
      CarErrorCode.PersonIsDriver,
      failedAddDriverResponseDueToDuplicate.useSerializer(CarErrorTestDto.serializer()).code,
    )

    val addedPassengerResponse = addPassenger(
      httpHandler = httpHandler,
      carId = car.id,
      personId = passenger.id,
    )

    Assertions.assertEquals(Status.OK, addedPassengerResponse.status)

    val failedAddedSecondPassengerAsPersonIsDriver = addPassenger(
      httpHandler = httpHandler,
      carId = car.id,
      personId = driver.id,
    )

    Assertions.assertEquals(Status.BAD_REQUEST, failedAddedSecondPassengerAsPersonIsDriver.status)
    Assertions.assertEquals(
      CarErrorCode.PersonIsDriver,
      failedAddedSecondPassengerAsPersonIsDriver.useSerializer(CarErrorTestDto.serializer()).code,
    )

    val updatedCar = listCars(
      httpHandler = httpHandler,
    )

    Assertions.assertEquals(2, updatedCar.items[0].passengers.size)

    val failedRemoveDriverRequestAsPassengerWasPassedAsDriverResponse = removeDriver(
      httpHandler = httpHandler,
      carId = car.id,
      personId = passenger.id,
    )

    Assertions.assertEquals(Status.BAD_REQUEST, failedRemoveDriverRequestAsPassengerWasPassedAsDriverResponse.status)
    Assertions.assertEquals(
      CarErrorCode.PersonIsNotDriver,
      failedRemoveDriverRequestAsPassengerWasPassedAsDriverResponse.useSerializer(CarErrorTestDto.serializer()).code,
    )

    val failedRemovePassengerAsDriverWasPassedAsPassengerResponse = removePassenger(
      httpHandler = httpHandler,
      carId = car.id,
      personId = driver.id,
    )

    Assertions.assertEquals(Status.BAD_REQUEST, failedRemovePassengerAsDriverWasPassedAsPassengerResponse.status)
    Assertions.assertEquals(
      CarErrorCode.PersonIsNotPassenger,
      failedRemovePassengerAsDriverWasPassedAsPassengerResponse.useSerializer(CarErrorTestDto.serializer()).code,
    )

    val removeDriverResponse = removeDriver(
      httpHandler = httpHandler,
      carId = car.id,
      personId = driver.id,
    )

    Assertions.assertEquals(Status.OK, removeDriverResponse.status)

    val removePassengerResponse = removePassenger(
      httpHandler = httpHandler,
      carId = car.id,
      personId = passenger.id,
    )

    Assertions.assertEquals(Status.OK, removePassengerResponse.status)

    val updatedCarAfterRemovalOfDriverAndPassenger = listCars(
      httpHandler = httpHandler,
    ).items[0]

    Assertions.assertEquals(0, updatedCarAfterRemovalOfDriverAndPassenger.passengers.size)
  }

  @Test
  fun `Car Capacity cannot be overwritten`() {
    val httpHandler = createTestApp()

    val car = createCar(
      httpHandler = httpHandler,
      createRequest = CreateOrEditCarRequest(
        regNr = "DR94054",
        passengerCapacity = 1,
        carType = CarType.Person,
      ),
    ).useSerializer(CarDto.serializer())

    val driver = createPerson(
      httpHandler = httpHandler,
      createRequest = CreateOrEditPersonRequest(
        birthDay = LocalDate.parse("1980-01-01"),
        hasLicense = true,
        name = "Frank Franksen",
      ),
    ).useSerializer(PersonDto.serializer())

    val successfullDriverResponse = addDriver(
      httpHandler = httpHandler,
      carId = car.id,
      personId = driver.id,
    )

    Assertions.assertEquals(Status.OK, successfullDriverResponse.status)

    val secondDriver = createPerson(
      httpHandler = httpHandler,
      createRequest = CreateOrEditPersonRequest(
        birthDay = LocalDate.parse("1980-01-01"),
        hasLicense = true,
        name = "Endre Franksen",
      ),
    ).useSerializer(PersonDto.serializer())

    val failedDriverResponse = addDriver(
      httpHandler = httpHandler,
      carId = car.id,
      personId = secondDriver.id,
    )

    Assertions.assertEquals(Status.BAD_REQUEST, failedDriverResponse.status)

    Assertions.assertEquals(
      CarErrorCode.NoAvailableSeats,
      failedDriverResponse.useSerializer(CarErrorTestDto.serializer()).code,
    )
  }
}
