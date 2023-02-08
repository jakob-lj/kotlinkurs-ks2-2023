package no.liflig.ks2kurs.services.car

import no.liflig.ks2kurs.common.http4k.errors.CarError
import no.liflig.ks2kurs.common.http4k.errors.CarErrorCode
import no.liflig.ks2kurs.services.car.domain.Car
import no.liflig.ks2kurs.services.car.domain.CarId
import no.liflig.ks2kurs.services.car.domain.CarRepository
import no.liflig.ks2kurs.services.car.dtos.CreateOrEditCarRequest
import no.liflig.ks2kurs.services.person.domain.Person
import no.liflig.ks2kurs.services.person.domain.PersonId

class CarService(
  private val carRepository: CarRepository,
) {
  suspend fun getAllCars(): List<Car> {
    return carRepository.getAll()
  }

  suspend fun create(request: CreateOrEditCarRequest): Car {
    val existingCar = carRepository.getAll().find { it.regNr == request.regNr }

    if (existingCar != null) {
      throw CarError(CarErrorCode.CarAlreadyExists)
    }

    return carRepository.create(
      Car.create(
        regNr = request.regNr,
        passengerCapacity = request.passengerCapacity,
        drivers = emptyList(),
        passengers = emptyList(),
        carType = request.carType,
      ),
    )
  }

  suspend fun edit(request: CreateOrEditCarRequest, carId: CarId): Car {
    val existingCars = carRepository.getAll().filter { it.regNr == request.regNr }

    if (existingCars.size > 1 || existingCars.size == 1 && existingCars[0].id != carId) {
      throw CarError(CarErrorCode.CarAlreadyExists)
    }

    val car = carRepository.get(carId) ?: throw CarError(CarErrorCode.CarNotFound)

    val updatedWithCorrectRegNr = car.updateRegNr(request.regNr)
    val updatedWithCorrectCapacity = updatedWithCorrectRegNr.updateCapacity(request.passengerCapacity)
    val updatedWithCorrectCarType = updatedWithCorrectCapacity.updateCarType(request.carType)

    return carRepository.update(updatedWithCorrectCarType)
  }

  suspend fun addDriver(person: Person, carId: CarId): Car {
    val car = carRepository.get(carId) ?: throw CarError(CarErrorCode.CarNotFound)

    if (!person.hasLicense) {
      throw CarError(CarErrorCode.PersonDoesNotHaveValidCertificate)
    }

    if (car.availableSeats < 1) {
      throw CarError(CarErrorCode.NoAvailableSeats)
    }

    return carRepository.update(
      car.addDriver(person.id),
    )
  }

  suspend fun addPassenger(person: Person, carId: CarId): Car {
    val car = carRepository.get(carId) ?: throw CarError(CarErrorCode.CarNotFound)

    return carRepository.update(
      car.addPassenger(person.id),
    )
  }

  suspend fun removePassenger(personId: PersonId, carId: CarId): Car {
    // we already know that person exists

    val existingCar = carRepository.get(carId) ?: throw CarError(CarErrorCode.CarNotFound)

    return carRepository.update(existingCar.removePassenger(personId))
  }

  suspend fun removeDriver(personId: PersonId, carId: CarId): Car {
    // we already know person exists
    val existingCar = carRepository.get(carId) ?: throw CarError(CarErrorCode.CarNotFound)

    return carRepository.update(existingCar.removeDriver(personId))
  }

  suspend fun getCarsWithPerson(personId: PersonId): List<Car> {
    return getAllCars().filter {
      it.drivers.contains(personId) || it.passengers.contains(personId)
    }
  }

  suspend fun removeFromCar(personId: PersonId, carId: CarId) {
    val car = carRepository.get(carId) ?: throw CarError(CarErrorCode.CarNotFound)

    carRepository.update(car.removePassenger(personId).removeDriver(personId))
  }
}
