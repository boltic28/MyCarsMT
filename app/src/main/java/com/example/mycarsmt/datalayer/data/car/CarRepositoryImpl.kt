package com.example.mycarsmt.datalayer.data.car

import com.example.mycarsmt.businesslayer.Car
import io.reactivex.Single

class CarRepositoryImpl(private val dao: CarDao) :
    CarRepository {

    override fun insert(car: Car): Single<Long> =
        dao.insert(getEntityFrom(car))

    override fun getById(id: Long): Single<Car> =
        dao.getById(id).map { entity ->
            getCarFrom(entity)
        }

    override fun update(car: Car): Single<Int> =
        dao.update(getEntityFrom(car))

    override fun delete(car: Car): Single<Int> =
        dao.delete(getEntityFrom(car))

    override fun getAll(): Single<List<Car>> =
        dao.getAll().map { listOfEntities ->
            listOfEntities.map { entity ->
                getCarFrom(entity)
            }
        }
}