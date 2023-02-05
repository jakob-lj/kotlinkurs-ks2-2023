@file:UseSerializers(LocalDateSerializer::class)

package no.liflig.ks2kurs.services.person.dtos

import kotlinx.serialization.UseSerializers
import no.liflig.ks2kurs.common.http4k.lenses.createBodyLens
import no.liflig.ks2kurs.common.serialization.LocalDateSerializer
import java.time.LocalDate

@kotlinx.serialization.Serializable
data class CreateOrEditPersonRequest(
  val name: String,
  val birthDay: LocalDate,
) {
  companion object {
    val bodyLens by lazy { createBodyLens(serializer()) }
    val example = CreateOrEditPersonRequest(
      name = "Ola Normann",
      birthDay = LocalDate.parse("2000-01-01"),
    )
  }
}
