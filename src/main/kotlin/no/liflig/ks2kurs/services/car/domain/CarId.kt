package no.liflig.ks2kurs.services.car.domain

import no.liflig.documentstore.entity.EntityId
import no.liflig.ks2kurs.common.serialization.UuidEntityIdSerializer
import java.util.UUID

@kotlinx.serialization.Serializable(with = CarIdSerializer::class)
data class CarId(val value: UUID = UUID.randomUUID()) : EntityId {
  constructor(id: String) : this(UUID.fromString(id))
}

object CarIdSerializer : UuidEntityIdSerializer<CarId>(::CarId)
