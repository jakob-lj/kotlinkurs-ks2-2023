package no.liflig.ks2kurs.services.person.dtos

import no.liflig.ks2kurs.common.http4k.lenses.createBodyLens

@kotlinx.serialization.Serializable
data class PersonsDto(val items: List<PersonDto>) {
  companion object {
    val bodyLens by lazy { createBodyLens(serializer()) }
    val example = PersonsDto(
      items = listOf(PersonDto.example),
    )
  }
}
