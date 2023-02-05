package no.liflig.ks2kurs.services.person.domain

import no.liflig.documentstore.entity.VersionedEntity
import no.liflig.ks2kurs.common.domain.AbstractKursSearchRepository
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi

class PersonSearchQuery()

class PersonSearchRepositoryJdbi(jdbi: Jdbi) :
  AbstractKursSearchRepository<PersonId, Person, PersonSearchQuery>(
    jdbi,
    PersonRepositoryJdbi.SQL_TABLE_NAME,
    personSerializationAdapter,
  ) {
  override suspend fun search(query: PersonSearchQuery, handle: Handle?): List<VersionedEntity<Person>> {
    TODO("Not yet implemented")
  }
}
