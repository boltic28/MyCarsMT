package com.example.mycarsmt.domain.service.car

import com.example.mycarsmt.datalayer.data.car.CarDao
import com.example.mycarsmt.domain.Car
import io.reactivex.Single

class CarRepositoryImpl(private val carDao: CarDao) : CarRepository {

    override fun create(car: Car): Single<Long> {
        TODO("Not yet implemented")
    }

    override fun getById(id: Long): Single<Car> {
        TODO("Not yet implemented")
    }

    override fun update(car: Car): Single<Int> {
        TODO("Not yet implemented")
    }

    override fun delete(car: Car): Single<Int> {
        TODO("Not yet implemented")
    }

    override fun getAll(): Single<List<Car>> {
        TODO("Not yet implemented")
    }
}

