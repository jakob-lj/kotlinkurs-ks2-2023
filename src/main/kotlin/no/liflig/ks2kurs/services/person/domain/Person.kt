@file:UseSerializers(LocalDateSerializer::class)

package no.liflig.ks2kurs.services.person.domain

import kotlinx.serialization.UseSerializers
import no.liflig.ks2kurs.common.db.Entity
import no.liflig.ks2kurs.common.serialization.LocalDateSerializer

@kotlinx.serialization.Serializable
data class Person(
  override val id: PersonId,
  // TODO extract name to firstname, lastname
  val name: String,

  // TODO add birthday
) : Entity<PersonId> {

  // TODO getter for full name

  fun edit(name: String = this.name): Person = update(
    name = name,
  )

  private fun update(name: String = this.name): Person = Person(
    id = id,
    name = name,
  )

  companion object {
    fun create(
      id: PersonId = PersonId(),
      name: String,
    ): Person = Person(
      id = id,
      name = name,
    )
  }
}
