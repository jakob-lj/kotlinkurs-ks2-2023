@file:UseSerializers(LocalDateSerializer::class)

package no.liflig.ks2kurs.services.person.dtos

import kotlinx.serialization.UseSerializers
import no.liflig.ks2kurs.common.http4k.lenses.createBodyLens
import no.liflig.ks2kurs.common.serialization.LocalDateSerializer
import no.liflig.ks2kurs.services.person.domain.Person
import no.liflig.ks2kurs.services.person.domain.PersonId
import java.time.LocalDate

@kotlinx.serialization.Serializable
data class PersonDto(
  val id: PersonId,
  val name: String,
  val hasLicense: Boolean,
  val birthDay: LocalDate,
) {
  companion object {
    val bodyLens by lazy { createBodyLens(serializer()) }
    val example = PersonDto(
      id = PersonId("5831c867-75ab-4b36-9bb1-dfd34b4e58c5"),
      name = "Some name",
      birthDay = LocalDate.parse("2020-01-01"),
      hasLicense = true,
    )
  }
}

fun Person.toDto(): PersonDto = PersonDto(
  id = id,
  name = name,
  birthDay = LocalDate.now(), // TODO Set value
  hasLicense = true, // TODO set value
)
