package com.example.mycarsmt.domain.service.repair

import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.Repair
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface RepairService{

    fun create(repair: Repair): Single<Long>
    fun update(repair: Repair): Single<Int>
    fun delete(repair: Repair): Single<Int>
    fun getAll(): Single<List<Repair>>
    fun getById(id: Long): Single<Repair>
    fun getAllForCar(car: Car): Single<List<Repair>>
    fun getAllForPart(part: Part): Single<List<Repair>>

    fun getCarFor(repair: Repair): Maybe<Car>
    fun getPartFor(repair: Repair): Maybe<Part>
}