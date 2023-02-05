package no.liflig.ks2kurs.services.person

import no.liflig.ks2kurs.services.person.domain.PersonRepository

class PersonService(
  val personRepository: PersonRepository,
)
