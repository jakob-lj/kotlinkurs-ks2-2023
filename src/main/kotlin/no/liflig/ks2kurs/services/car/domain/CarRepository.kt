package no.liflig.ks2kurs.services.car.domain

import no.liflig.ks2kurs.common.db.CrudDao

typealias CarRepository = CrudDao<Car, CarId>
