package no.liflig.ks2kurs.services.car.dtos

import no.liflig.ks2kurs.common.http4k.lenses.createBodyLens
import no.liflig.ks2kurs.services.car.domain.Car

@kotlinx.serialization.Serializable
data class CarDto(
  val id: String,
  val regNr: String,
) {
  companion object {
    val bodyLens by lazy { createBodyLens(serializer()) }
    val example = CarDto(
      id = "f64809ea-2f23-45ff-a598-9e3f54d571bb",
      regNr = "DR94054",
    )
  }
}

fun Car.toDto(): CarDto = CarDto(
  id = id.value.toString(),
  regNr = regNr,
)
