package no.liflig.ks2kurs.services.person.domain

import no.liflig.documentstore.entity.AbstractEntityRoot

@kotlinx.serialization.Serializable
class Person(override val id: PersonId, val name: String) : AbstractEntityRoot<PersonId>() {

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
