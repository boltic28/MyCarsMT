package com.example.mycarsmt.domain.service.car

import com.example.mycarsmt.domain.Car
import io.reactivex.Single


interface CarRepository {

    fun create(car: Car): Single<Long>

    fun getById(id: Long): Single<Car>

    fun update(car: Car): Single<Int>

    fun delete(car: Car): Single<Int>

    fun getAll(): Single<List<Car>>
}