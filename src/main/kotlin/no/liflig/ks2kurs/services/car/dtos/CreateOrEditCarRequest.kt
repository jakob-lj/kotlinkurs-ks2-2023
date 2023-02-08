package no.liflig.ks2kurs.services.car.dtos

import no.liflig.ks2kurs.common.http4k.lenses.createBodyLens
import no.liflig.ks2kurs.services.car.domain.CarType

@kotlinx.serialization.Serializable
data class CreateOrEditCarRequest(
  val regNr: String,
  val carType: CarType,
  val passengerCapacity: Int,
) {
  companion object {
    val bodyLens by lazy { createBodyLens(serializer()) }
    val example = CreateOrEditCarRequest(
      regNr = "DR94054",
      carType = CarType.Person,
      passengerCapacity = 4,
    )
  }
}
