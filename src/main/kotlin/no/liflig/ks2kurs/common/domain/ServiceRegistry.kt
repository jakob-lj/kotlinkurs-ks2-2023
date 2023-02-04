package no.liflig.ks2kurs.common.domain

import no.liflig.ks2kurs.services.car.domain.CarRepository

data class ServiceRegistry(
  val carRepository: CarRepository,
)
