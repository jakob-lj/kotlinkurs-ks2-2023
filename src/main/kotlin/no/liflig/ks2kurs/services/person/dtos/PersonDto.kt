package no.liflig.ks2kurs.services.person.dtos

import no.liflig.ks2kurs.common.http4k.lenses.createBodyLens
import no.liflig.ks2kurs.services.person.domain.Person
import no.liflig.ks2kurs.services.person.domain.PersonId

@kotlinx.serialization.Serializable
data class PersonDto(
  val id: PersonId,
  val name: String,
) {
  companion object {
    val bodyLens by lazy { createBodyLens(serializer()) }
    val example = PersonDto(
      id = PersonId("5831c867-75ab-4b36-9bb1-dfd34b4e58c5"),
      name = "Some name",
    )
  }
}

fun Person.toDto(): PersonDto = PersonDto(
  id = id,
  name = name,
)
