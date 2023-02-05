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
    return carRepository.getAll().map { it.item }
  }

  suspend fun create(request: CreateOrEditCarRequest): Car {
    val existingCar = carRepository.getAll().find { it.item.regNr == request.regNr }

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
    ).item
  }

  suspend fun edit(request: CreateOrEditCarRequest, carId: CarId): Car {
    val car = carRepository.get(carId) ?: throw CarError(CarErrorCode.CarNotFound)

    return carRepository.update(
      car.item.updateRegNr(regNr = request.regNr),
      previousVersion = car.version,
    ).item
  }

  suspend fun addDriver(person: Person, carId: CarId): Car {
    val car = carRepository.get(carId) ?: throw CarError(CarErrorCode.CarNotFound)

    if (!person.hasLicense) {
      throw CarError(CarErrorCode.PersonDoesNotHaveValidCertificate)
    }

    if (car.item.availableSeats < 1) {
      throw CarError(CarErrorCode.NoAvailableSeats)
    }

    return carRepository.update(
      car.item.addDriver(person.id),
      car.version,
    ).item
  }

  suspend fun addPassenger(person: Person, carId: CarId): Car {
    val car = carRepository.get(carId) ?: throw CarError(CarErrorCode.CarNotFound)

    return carRepository.update(
      car.item.addPassenger(person.id),
      car.version,
    ).item
  }

  suspend fun removePassenger(personId: PersonId, carId: CarId): Car {
    // we already know that person exists

    val existingCar = carRepository.get(carId) ?: throw CarError(CarErrorCode.CarNotFound)

    return carRepository.update(existingCar.item.removePassenger(personId), existingCar.version).item
  }

  suspend fun removeDriver(personId: PersonId, carId: CarId): Car {
    // we already know person exists
    val existingCar = carRepository.get(carId) ?: throw CarError(CarErrorCode.CarNotFound)

    return carRepository.update(existingCar.item.removeDriver(personId), existingCar.version).item
  }

  suspend fun getCarsWithPerson(personId: PersonId): List<Car> {
    return getAllCars().filter {
      it.drivers.contains(personId) || it.passengers.contains(personId)
    }
  }

  suspend fun removeFromCar(personId: PersonId, carId: CarId) {
    val car = carRepository.get(carId) ?: throw CarError(CarErrorCode.CarNotFound)

    carRepository.update(car.item.removePassenger(personId).removeDriver(personId), car.version)
  }
}
