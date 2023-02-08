package no.liflig.ks2kurs.services.person.domain

import no.liflig.ks2kurs.common.db.CrudDao


typealias PersonRepository = CrudDao<Person, PersonId>

