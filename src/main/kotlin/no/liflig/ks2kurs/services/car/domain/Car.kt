package no.liflig.ks2kurs.services.car.domain

import no.liflig.ks2kurs.common.db.Entity

data class Car private constructor(
  override val id: CarId,
  // TODO immutability
  var regNr: String,
  // TODO more attributes on car
) : Entity<CarId> {

  fun updateRegNr(regNr: String) = update(
    regNr = regNr,
  )

  private fun update(regNr: String = this.regNr) = Car(
    id = this.id,
    regNr = regNr,
  )

  companion object {
    fun create(
      id: CarId = CarId(),
      regNr: String,
    ): Car = Car(
      id = id,
      regNr = regNr,
    )
  }
}
