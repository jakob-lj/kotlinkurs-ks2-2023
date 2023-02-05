package no.liflig.ks2kurs.services.person

import no.liflig.ks2kurs.common.http4k.errors.PersonError
import no.liflig.ks2kurs.common.http4k.errors.PersonErrorCode
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
    return personRepository.create(
      Person.create(
        id = PersonId(),
        firstName = request.name.firstName(),
        lastName = request.name.lastName(),
        birthDay = request.birthDay,
        hasLicense = request.hasLicense,
      ),
    ).item
  }

  suspend fun edit(request: CreateOrEditPersonRequest, personId: PersonId): Person {
    val existingPerson = personRepository.get(personId) ?: throw PersonError(PersonErrorCode.PersonNotFound)

    return personRepository.update(
      existingPerson.item.edit(
        firstName = request.name.firstName(),
        request.name.lastName(),
      ),
      existingPerson.version,
    ).item
  }

  suspend fun getByFilter(filter: PersonServiceListFilter): List<Person> {
    val allPersons = personRepository.getAll()

    return allPersons
      .map { it.item }
      .filter {
        filter.hasLicense == null || it.hasLicense &&
          filter.birthYear == null || it.birthDay.year == filter.birthYear?.value
      }
  }
}

fun String.firstName(): String {
  val splittedText = this.split(" ")
  return splittedText.subList(0, splittedText.size - 1).joinToString(" ")
}

fun String.lastName(): String = this.split(" ").last()
