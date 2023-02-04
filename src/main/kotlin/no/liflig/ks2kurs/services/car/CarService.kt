package no.liflig.ks2kurs.services.car

import no.liflig.ks2kurs.services.car.domain.Car
import no.liflig.ks2kurs.services.car.domain.CarRepository

class CarService(
  private val carRepository: CarRepository,
) {
  suspend fun getAllCars(): List<Car> {
    return carRepository.getAll().map { it.item }
  }

  suspend fun create(car: Car): Car {
    // TODO check if car already exists -- if: throw CarAlreadyExists

    // TODO persist car

    return car
  }

  suspend fun edit(newCar: Car): Car {
    val existingCar = carRepository.get(newCar.id)!!.item // TODO throw CarNotFound if not exists

    // TODO use update pattern to update existing car

    existingCar.regNr = newCar.regNr

    // TODO update in persistence

    return existingCar
  }
}
