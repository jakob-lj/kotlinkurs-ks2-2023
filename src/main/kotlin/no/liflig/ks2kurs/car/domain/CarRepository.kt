package no.liflig.ks2kurs.car.domain

import no.liflig.documentstore.dao.CrudDao
import no.liflig.documentstore.entity.VersionedEntity
import no.liflig.ks2kurs.common.domain.Repository

typealias CarRepository = Repository<CarId, Car>

class CarRepositoryImpl(override val crudDao: CrudDao<CarId, Car>) : Repository<CarId, Car> {
  override suspend fun create(item: Car): VersionedEntity<Car> {
    TODO("Not yet implemented")
  }

  override suspend fun get(id: CarId): VersionedEntity<Car>? {
    TODO("Not yet implemented")
  }

  override suspend fun getAll(): List<VersionedEntity<Car>> {
    TODO("Not yet implemented")
  }

  override suspend fun delete(item: Car) {
    TODO("Not yet implemented")
  }

  override suspend fun update(item: VersionedEntity<Car>): VersionedEntity<Car> {
    TODO("Not yet implemented")
  }

}
