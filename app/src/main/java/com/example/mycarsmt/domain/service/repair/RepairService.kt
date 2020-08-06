package com.example.mycarsmt.domain.service.repair

import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.Repair
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface RepairService{

    fun create(repair: Repair): Single<Repair>
    fun update(repair: Repair): Single<Repair>
    fun delete(repair: Repair): Single<Int>
    fun readAll(): Flowable<List<Repair>>
    fun readById(id: Long): Flowable<Repair>
    fun readAllForCar(car: Car): Flowable<List<Repair>>
    fun readAllForPart(part: Part): Flowable<List<Repair>>

    fun getCarFor(repair: Repair): Maybe<Car>
    fun getPartFor(repair: Repair): Maybe<Part>
}