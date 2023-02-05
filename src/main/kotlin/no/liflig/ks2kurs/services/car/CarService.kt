package no.liflig.ks2kurs.services.car

import no.liflig.ks2kurs.services.car.domain.Car
import no.liflig.ks2kurs.services.car.domain.CarId
import no.liflig.ks2kurs.services.car.domain.CarRepository
import no.liflig.ks2kurs.services.car.dtos.CreateOrEditCarRequest
import no.liflig.ks2kurs.services.person.domain.PersonId

class CarService(
  private val carRepository: CarRepository,
) {
  suspend fun getAllCars(): List<Car> {
    return carRepository.getAll().map { it.item }
  }

  suspend fun create(request: CreateOrEditCarRequest): Car {
    // TODO check if car already exists -- if: throw CarAlreadyExists

    // TODO persist car

    return Car.create(
      regNr = request.regNr,
    )
  }

  suspend fun edit(request: CreateOrEditCarRequest, carId: CarId): Car {
    val car = carRepository.get(carId)!!.item // TODO throw CarNotFound if not exists

    // TODO use update pattern to update existing car

    car.regNr = request.regNr

    // TODO update in persistence

    return car
  }

  suspend fun addDriver(personId: PersonId, carId: CarId): Car {
    // TODO implement method
    return Car.create(
      id = carId,
      regNr = "DR94054",
    )
  }

  suspend fun getCarsWithPerson(personId: PersonId): List<Car> {
    // TODO Implement

    return emptyList()
  }

  suspend fun removeFromCar(personId: PersonId, carId: CarId) {
    // TODO Implement
  }
}
