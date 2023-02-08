package no.liflig.ks2kurs.services.car.dtos

import no.liflig.ks2kurs.common.http4k.lenses.createBodyLens
import no.liflig.ks2kurs.services.car.domain.Car
import no.liflig.ks2kurs.services.car.domain.CarId
import no.liflig.ks2kurs.services.car.domain.CarType
import no.liflig.ks2kurs.services.person.domain.PersonId

@kotlinx.serialization.Serializable
data class CarDto(
  val id: CarId,
  val regNr: String,
  val passengers: List<PersonId>,
  val passengerCapacity: Int,
  val carType: CarType,
) {
  companion object {
    val bodyLens by lazy { createBodyLens(serializer()) }
    val example = CarDto(
      id = CarId("f64809ea-2f23-45ff-a598-9e3f54d571bb"),
      regNr = "DR94054",
      passengers = listOf(PersonId("f64809ea-2f23-45ff-a598-9e3f54d571bb")),
      passengerCapacity = 5,
      carType = CarType.Person,
    )
  }
}

fun Car.toDto(): CarDto = CarDto(
  id = id,
  regNr = regNr,
  passengers = drivers + passengers,
  passengerCapacity = passengerCapacity,
  carType = carType,
)
