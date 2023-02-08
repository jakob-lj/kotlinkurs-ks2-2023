package no.liflig.ks2kurs.services.person.domain

import no.liflig.ks2kurs.common.db.EntityId
import no.liflig.ks2kurs.common.serialization.UuidEntityIdSerializer
import java.util.*

@kotlinx.serialization.Serializable(with = PersonIdSerializer::class)
data class PersonId(val id: UUID) : EntityId {
  constructor() : this(UUID.randomUUID())
  constructor(id: String) : this(UUID.fromString(id))

  override fun toString(): String {
    return id.toString()
  }
}

object PersonIdSerializer : UuidEntityIdSerializer<PersonId>(::PersonId)
