package no.liflig.ks2kurs.services.car.domain

import no.liflig.documentstore.entity.VersionedEntity
import no.liflig.ks2kurs.common.domain.AbstractKursSearchRepository
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi

class CarSearchQuery()

class CarSearchRepositoryJdbi(jdbi: Jdbi) : AbstractKursSearchRepository<CarId, Car, CarSearchQuery>(
  jdbi = jdbi,
  sqlTableName = CarRepositoryJdbi.SQL_TABLE_NAME,
  serializationAdapter = carSerializerAdapter,
) {
  override suspend fun search(query: CarSearchQuery, handle: Handle?): List<VersionedEntity<Car>> {
    TODO("Not yet implemented")
  }
}
