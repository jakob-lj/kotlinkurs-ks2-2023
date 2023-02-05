package no.liflig.ks2kurs.services.person.domain

import no.liflig.documentstore.dao.CrudDao
import no.liflig.documentstore.entity.Version
import no.liflig.documentstore.entity.VersionedEntity
import no.liflig.ks2kurs.common.domain.AbstractKursSearchRepository
import no.liflig.ks2kurs.common.domain.Repository

typealias PersonRepository = Repository<PersonId, Person, PersonSearchQuery>

class PersonRepositoryJdbi(
  override val crudDao: CrudDao<PersonId, Person>,
  override val searchRepo: AbstractKursSearchRepository<PersonId, Person, PersonSearchQuery>,
) : Repository<PersonId, Person, PersonSearchQuery> {

  companion object {
    const val SQL_TABLE_NAME = "persons"
  }

  override suspend fun create(item: Person): VersionedEntity<Person> {
    return crudDao.create(item)
  }

  override suspend fun get(id: PersonId): VersionedEntity<Person>? {
    return crudDao.get(id)
  }

  override suspend fun getAll(): List<VersionedEntity<Person>> {
    return searchRepo.getAll()
  }

  override suspend fun update(item: Person, previousVersion: Version): VersionedEntity<Person> {
    return crudDao.update(item, previousVersion)
  }

  override suspend fun delete(item: Person, previousVersion: Version) {
    return crudDao.delete(item.id, previousVersion)
  }
}
