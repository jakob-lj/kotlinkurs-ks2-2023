package no.liflig.ks2kurs.services.car.domain

import no.liflig.documentstore.entity.AbstractEntityRoot
import no.liflig.ks2kurs.common.serialization.KotlinXSerializationAdapter

@kotlinx.serialization.Serializable
data class Car(
  override val id: CarId,
  // TODO immutability
  var regNr: String,
) : AbstractEntityRoot<CarId>()

val carSerializerAdapter = KotlinXSerializationAdapter(Car.serializer())
