package no.liflig.ks2kurs.bff

import no.liflig.ks2kurs.common.http4k.errors.CarErrorCode
import no.liflig.ks2kurs.common.utils.createTestApp
import no.liflig.ks2kurs.common.utils.useSerializer
import no.liflig.ks2kurs.services.car.domain.CarType
import no.liflig.ks2kurs.services.car.dtos.CarDto
import no.liflig.ks2kurs.services.car.dtos.CreateOrEditCarRequest
import org.http4k.core.Status
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CarTest {
  @Test
  fun `Creating and editing car test`() {
    val httpHandler = createTestApp()

    val car1RegNr = "DR94054"
    val car2RegNr = "DR94053"

    val carResponse = createCar(
      httpHandler = httpHandler,
      createRequest = CreateOrEditCarRequest(
        regNr = car1RegNr,
        passengerCapacity = 4,
        carType = CarType.Person,
      ),
    )

    Assertions.assertEquals(Status.OK, carResponse.status)

    val car = carResponse.useSerializer(CarDto.serializer())

    Assertions.assertEquals(CarType.Person, car.carType)

    val listedCars = listCars(
      httpHandler = httpHandler,
    )

    Assertions.assertEquals(1, listedCars.items.size)
    Assertions.assertEquals(4, listedCars.items[0].passengerCapacity)

    val failedCarRequestDueToSameRegNr = createCar(
      httpHandler = httpHandler,
      createRequest = CreateOrEditCarRequest(
        regNr = car1RegNr,
        passengerCapacity = 3,
        carType = CarType.Person,
      ),
    )

    Assertions.assertEquals(Status.BAD_REQUEST, failedCarRequestDueToSameRegNr.status)
    Assertions.assertEquals(
      CarErrorCode.CarAlreadyExists,
      failedCarRequestDueToSameRegNr.useSerializer(CarErrorTestDto.serializer()).code,
    )

    val editedFristCar = editCar(
      httpHandler = httpHandler,
      carId = car.id,
      editRequest = CreateOrEditCarRequest(
        regNr = car1RegNr,
        // This has changed
        passengerCapacity = 1,
        // This has changed
        carType = CarType.Cargo,
      ),
    )

    Assertions.assertEquals(Status.OK, editedFristCar.status)

    val updatedCar1 = editedFristCar.useSerializer(CarDto.serializer())

    val updatedCarList = listCars(
      httpHandler = httpHandler,
    )

    Assertions.assertEquals(
      updatedCar1,
      updatedCarList.items[0],
    )

    Assertions.assertEquals(1, updatedCarList.items[0].passengerCapacity)
    Assertions.assertEquals(CarType.Cargo, updatedCarList.items[0].carType)

    val secondCar = createCar(
      httpHandler = httpHandler,
      createRequest = CreateOrEditCarRequest(
        regNr = car2RegNr,
        passengerCapacity = 5,
        carType = CarType.Person,
      ),
    )

    val car2 = secondCar.useSerializer(CarDto.serializer())

    Assertions.assertEquals(Status.OK, secondCar.status)

    val failedEditRequestAsCar2TriesToEditRegNrToAnotherCarsRegNr = editCar(
      httpHandler = httpHandler,
      carId = car2.id,
      editRequest = CreateOrEditCarRequest(
        regNr = car1RegNr,
        passengerCapacity = 5,
        carType = CarType.Person,
      ),
    )

    Assertions.assertEquals(Status.BAD_REQUEST, failedEditRequestAsCar2TriesToEditRegNrToAnotherCarsRegNr.status)
  }
}
