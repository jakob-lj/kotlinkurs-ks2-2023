package no.liflig.ks2kurs.common.domain

import no.liflig.documentstore.dao.AbstractSearchRepository
import no.liflig.documentstore.dao.SerializationAdapter
import no.liflig.documentstore.entity.EntityId
import no.liflig.documentstore.entity.EntityRoot
import no.liflig.documentstore.entity.VersionedEntity
import org.jdbi.v3.core.Jdbi

abstract class AbstractKursSearchRepository<I : EntityId, A : EntityRoot<I>, Q>(
  jdbi: Jdbi,
  sqlTableName: String,
  serializationAdapter: SerializationAdapter<A>,
) : AbstractSearchRepository<I, A, Q>(
  jdbi = jdbi,
  sqlTableName = sqlTableName,
  serializationAdapter = serializationAdapter,
) {
  suspend fun getAll(): List<VersionedEntity<A>> = getByPredicate()
}
