@file:UseSerializers(LocalDateSerializer::class)

package no.liflig.ks2kurs.services.person.dtos

import kotlinx.serialization.UseSerializers
import no.liflig.ks2kurs.common.http4k.lenses.createBodyLens
import no.liflig.ks2kurs.common.serialization.LocalDateSerializer

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
