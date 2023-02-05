package no.liflig.ks2kurs.services.person

import no.liflig.ks2kurs.services.person.domain.Person
import no.liflig.ks2kurs.services.person.domain.PersonId
import no.liflig.ks2kurs.services.person.domain.PersonRepository
import no.liflig.ks2kurs.services.person.dtos.CreateOrEditPersonRequest
import java.time.Year

class PersonServiceListFilter(val birthYear: Year?, val hasLicense: Boolean?)

class PersonService(
  val personRepository: PersonRepository,
) {
  suspend fun create(request: CreateOrEditPersonRequest): Person {
    // TODO persist
    return Person.create(
      id = PersonId(),
      // TODO split into firstname and lastname (do not make changes in the request api)
      name = request.name,
    )
  }

  suspend fun edit(request: CreateOrEditPersonRequest, personId: PersonId): Person {
    // TODO throw PersonNotFound if not exist

    // TODO update in persistence
    return Person.create(
      id = personId,
      name = request.name,
    )
  }

  suspend fun getByFilter(filter: PersonServiceListFilter): List<Person> {
    // TODO get all person!

    // TODO use filters to filter request

    return listOf()
  }
}
