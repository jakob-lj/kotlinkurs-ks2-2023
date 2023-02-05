@file:UseSerializers(LocalDateSerializer::class)

package no.liflig.ks2kurs.services.person.domain

import kotlinx.serialization.UseSerializers
import no.liflig.documentstore.entity.AbstractEntityRoot
import no.liflig.ks2kurs.common.serialization.LocalDateSerializer
import java.time.LocalDate

@kotlinx.serialization.Serializable
data class Person(
  override val id: PersonId,
  val firstName: String,
  val lastName: String,
  val birthDay: LocalDate,
  val hasLicense: Boolean,
) : AbstractEntityRoot<PersonId>() {

  val fullName: String
    get() = "$firstName $lastName"

  fun edit(
    firstName: String = this.firstName,
    lastName: String = this.lastName,
    hasLicense: Boolean = this.hasLicense,
  ): Person = update(
    firstName = firstName,
    lastName = lastName,
    hasLicense = hasLicense,
  )

  private fun update(
    firstName: String = this.firstName,
    lastName: String = this.lastName,
    birthDay: LocalDate = this.birthDay,
    hasLicense: Boolean = this.hasLicense,
  ): Person = Person(
    id = id,
    firstName = firstName,
    lastName = lastName,
    birthDay = birthDay,
    hasLicense = hasLicense,
  )

  companion object {
    fun create(
      id: PersonId = PersonId(),
      firstName: String,
      lastName: String,
      birthDay: LocalDate,
      hasLicense: Boolean,
    ): Person = Person(
      id = id,
      firstName = firstName,
      lastName = lastName,
      birthDay = birthDay,
      hasLicense,
    )
  }
}
