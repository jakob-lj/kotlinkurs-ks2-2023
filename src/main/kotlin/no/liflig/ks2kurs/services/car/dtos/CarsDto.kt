package no.liflig.ks2kurs.services.car.dtos

import no.liflig.ks2kurs.common.http4k.lenses.createBodyLens

@kotlinx.serialization.Serializable
data class CarsDto(
  val items: List<CarDto>,
) {
  companion object {
    val bodyLens by lazy { createBodyLens(serializer()) }
    val example = CarsDto(items = listOf(CarDto.example))
  }
}
