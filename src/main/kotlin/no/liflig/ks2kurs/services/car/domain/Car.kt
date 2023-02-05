package no.liflig.ks2kurs.services.car.domain

import no.liflig.documentstore.entity.AbstractEntityRoot
import no.liflig.ks2kurs.common.http4k.errors.CarError
import no.liflig.ks2kurs.common.http4k.errors.CarErrorCode
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

  val availableSeats: Int
    get() = passengerCapacity - drivers.size - passengers.size

  fun addDriver(personId: PersonId): Car {
    if (this.drivers.contains(personId)) {
      throw CarError(CarErrorCode.PersonIsDriver)
    }

    if (this.passengers.contains(personId)) {
      throw CarError(CarErrorCode.PersonIsPassenger)
    }

    return update(
      drivers = drivers + listOf(personId),
    )
  }

  fun addPassenger(personId: PersonId): Car {
    if (this.drivers.contains(personId)) {
      throw CarError(CarErrorCode.PersonIsDriver)
    }

    if (this.passengers.contains(personId)) {
      throw CarError(CarErrorCode.PersonIsPassenger)
    }

    return update(
      passengers = passengers + listOf(personId),
    )
  }

  fun removeDriver(personId: PersonId): Car {
    if (!this.drivers.contains(personId)) {
      throw CarError(CarErrorCode.PersonIsNotDriver)
    }

    return update(
      drivers = drivers.filter { it != personId },
    )
  }

  fun removePassenger(personId: PersonId): Car {
    if (!this.passengers.contains(personId)) {
      throw CarError(CarErrorCode.PersonIsNotPassenger)
    }

    return update(
      passengers = passengers.filter { it != personId },
    )
  }

  fun updateRegNr(regNr: String) = update(
    regNr = regNr,
  )

  fun updateCapacity(capacity: Int): Car {
    if (capacity < passengers.size + drivers.size) {
      throw CarError(CarErrorCode.CarCapacityTooLowRemoveDriversAndPassengers)
    }
    return update(
      passengerCapacity = capacity,
    )
  }

  fun updateCarType(carType: CarType) = update(
    carType = carType,
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
