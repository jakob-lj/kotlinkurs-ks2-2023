package no.liflig.ks2kurs.bff.car.routes.dtos

import no.liflig.ks2kurs.common.http4k.lenses.createBodyLens
import no.liflig.ks2kurs.services.person.domain.PersonId

@kotlinx.serialization.Serializable
data class AddDriverOrPassengerRequest(val personId: PersonId) {
  companion object {
    val bodyLens by lazy { createBodyLens(serializer()) }

    val example = AddDriverOrPassengerRequest(
      personId = PersonId("0b14731c-8e4e-47a7-ae45-df27aaafba25"),
    )
  }
}
