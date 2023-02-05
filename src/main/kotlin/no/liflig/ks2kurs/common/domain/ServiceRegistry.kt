package no.liflig.ks2kurs.common.domain

import no.liflig.ks2kurs.services.car.CarService
import no.liflig.ks2kurs.services.car.domain.CarRepository
import no.liflig.ks2kurs.services.person.PersonService
import no.liflig.ks2kurs.services.person.domain.PersonRepository

data class ServiceRegistry(
  // Car
  val carRepository: CarRepository,
  val carService: CarService,

  // Person
  val personRepository: PersonRepository,
  val personService: PersonService,
)
