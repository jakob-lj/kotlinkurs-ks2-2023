package no.liflig.ks2kurs.car.domain

import java.util.UUID

data class CarId(val value: UUID = UUID.randomUUID()) {
  constructor(id: String) : this(UUID.fromString(id))
}
