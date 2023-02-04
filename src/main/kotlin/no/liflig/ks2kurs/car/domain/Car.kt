package no.liflig.ks2kurs.car.domain

import no.liflig.documentstore.entity.EntityRoot

@kotlinx.serialization.Serializable
data class Car(override val id: CarId, val regNr: String) : EntityRoot<CarId>
