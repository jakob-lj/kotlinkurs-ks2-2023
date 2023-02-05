package no.liflig.ks2kurs.services.car.domain

import no.liflig.documentstore.entity.AbstractEntityRoot
import no.liflig.ks2kurs.common.serialization.KotlinXSerializationAdapter
import no.liflig.ks2kurs.services.person.domain.PersonId

@kotlinx.serialization.Serializable
data class Car private constructor(
  override val id: CarId,
  val regNr: String,
  val passengerCapacity: Int,
  val drivers: List<PersonId>,
  val passengers: List<PersonId>,
  val carType: CarType,
) : AbstractEntityRoot<CarId>() {

  fun updateRegNr(regNr: String) = update(
    regNr = regNr,
  )

  private fun update(
    regNr: String = this.regNr,
    passengerCapacity: Int = this.passengerCapacity,
    drivers: List<PersonId> = this.drivers,
    passengers: List<PersonId> = this.passengers,
    carType: CarType = this.carType,
  ) = Car(
    id = this.id,
    regNr = regNr,
    passengerCapacity = passengerCapacity,
    drivers = drivers,
    passengers = passengers,
    carType = carType,
  )

  companion object {
    fun create(
      id: CarId = CarId(),
      regNr: String,
      passengerCapacity: Int,
      drivers: List<PersonId>,
      passengers: List<PersonId>,
      carType: CarType,
    ): Car = Car(
      id = id,
      regNr = regNr,
      passengerCapacity = passengerCapacity,
      drivers = drivers,
      passengers = passengers,
      carType,
    )
  }
}

val carSerializerAdapter = KotlinXSerializationAdapter(Car.serializer())
