package no.liflig.ks2kurs.services.car.domain

import no.liflig.documentstore.entity.UuidEntityId
import no.liflig.ks2kurs.common.serialization.UuidEntityIdSerializer
import java.util.UUID

@kotlinx.serialization.Serializable(with = CarIdSerializer::class)
data class CarId(override val id: UUID) : UuidEntityId {
  constructor(id: String) : this(UUID.fromString(id))
  constructor() : this(UUID.randomUUID())

  override fun toString(): String {
    return id.toString()
  }
}

object CarIdSerializer : UuidEntityIdSerializer<CarId>(::CarId)
