package com.example.mycarsmt.datalayer.data.car

import com.example.mycarsmt.businesslayer.Car
import io.reactivex.Single


interface CarRepository {

    fun insert(car: Car): Single<Long>

    fun getById(id: Long): Single<Car>

    fun update(car: Car): Single<Int>

    fun delete(car: Car): Single<Int>

    fun getAll(): Single<List<Car>>
}