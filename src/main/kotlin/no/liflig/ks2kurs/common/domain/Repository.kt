package no.liflig.ks2kurs.common.domain

import no.liflig.documentstore.dao.CrudDao
import no.liflig.documentstore.entity.EntityId
import no.liflig.documentstore.entity.EntityRoot
import no.liflig.documentstore.entity.Version
import no.liflig.documentstore.entity.VersionedEntity

interface Repository<IT : EntityId, T : EntityRoot<IT>, Q> {

  val crudDao: CrudDao<IT, T>
  val searchRepo: AbstractKursSearchRepository<IT, T, Q>
  suspend fun create(item: T): VersionedEntity<T>

  suspend fun get(id: IT): VersionedEntity<T>?

  suspend fun getAll(): List<VersionedEntity<T>>

  suspend fun update(item: T, previousVersion: Version): VersionedEntity<T>

  suspend fun delete(item: T, previousVersion: Version)
}
