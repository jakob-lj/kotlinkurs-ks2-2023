package no.liflig.ks2kurs.services.car.dtos

import no.liflig.ks2kurs.common.http4k.lenses.createBodyLens

@kotlinx.serialization.Serializable
data class CreateOrEditCarRequest(
  val regNr: String,
) {
  companion object {
    val bodyLens by lazy { createBodyLens(serializer()) }
    val example = CreateOrEditCarRequest(
      regNr = "DR94054",
    )
  }
}
