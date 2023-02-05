package no.liflig.ks2kurs.services.car.domain

import no.liflig.documentstore.dao.CrudDao
import no.liflig.documentstore.entity.Version
import no.liflig.documentstore.entity.VersionedEntity
import no.liflig.ks2kurs.common.domain.Repository

typealias CarRepository = Repository<CarId, Car, CarSearchQuery>

class CarRepositoryJdbi(override val crudDao: CrudDao<CarId, Car>, override val searchRepo: CarSearchRepositoryJdbi) :
  Repository<CarId, Car, CarSearchQuery> {
  companion object {
    const val SQL_TABLE_NAME = "cars"
  }

  override suspend fun create(item: Car): VersionedEntity<Car> {
    return crudDao.create(item)
  }

  override suspend fun get(id: CarId): VersionedEntity<Car>? {
    return crudDao.get(id)
  }

  override suspend fun getAll(): List<VersionedEntity<Car>> {
    return searchRepo.getAll()
  }

  override suspend fun delete(item: Car, previousVersion: Version) {
    crudDao.delete(item.id, previousVersion)
  }

  override suspend fun update(item: Car, previousVersion: Version): VersionedEntity<Car> {
    return crudDao.update(item, previousVersion)
  }
}
