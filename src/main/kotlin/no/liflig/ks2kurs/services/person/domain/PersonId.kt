package no.liflig.ks2kurs.services.person.domain

import no.liflig.documentstore.entity.UuidEntityId
import no.liflig.ks2kurs.common.serialization.UuidEntityIdSerializer
import java.util.*

@kotlinx.serialization.Serializable(with = PersonIdSerializer::class)
class PersonId(override val id: UUID) : UuidEntityId {
  constructor() : this(UUID.randomUUID())
  constructor(id: String) : this(UUID.fromString(id))

  override fun toString(): String {
    return id.toString()
  }
}

object PersonIdSerializer : UuidEntityIdSerializer<PersonId>(::PersonId)
