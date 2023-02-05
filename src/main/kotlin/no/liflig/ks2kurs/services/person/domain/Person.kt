@file:UseSerializers(LocalDateSerializer::class)

package no.liflig.ks2kurs.services.person.domain

import kotlinx.serialization.UseSerializers
import no.liflig.documentstore.entity.AbstractEntityRoot
import no.liflig.ks2kurs.common.serialization.LocalDateSerializer

@kotlinx.serialization.Serializable
class Person(
  override val id: PersonId,
  // TODO extract name to firstname, lastname
  val name: String,

  // TODO add birthday
) : AbstractEntityRoot<PersonId>() {

  // TODO getter for full name

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
