package no.liflig.ks2kurs.services.person.dtos

import no.liflig.ks2kurs.common.http4k.lenses.createBodyLens

@kotlinx.serialization.Serializable
data class CreateOrEditPersonRequest(
  val name: String,
) {
  companion object {
    val bodyLens by lazy { createBodyLens(serializer()) }
    val example = CreateOrEditPersonRequest(
      name = "Ola Normann",
    )
  }
}
