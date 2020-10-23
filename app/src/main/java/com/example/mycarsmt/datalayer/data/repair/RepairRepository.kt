package com.example.mycarsmt.datalayer.data.repair

import com.example.mycarsmt.businesslayer.Car
import com.example.mycarsmt.businesslayer.Part
import com.example.mycarsmt.businesslayer.Repair
import io.reactivex.Maybe
import io.reactivex.Single

interface RepairRepository{

    fun insert(repair: Repair): Single<Long>
    fun update(repair: Repair): Single<Int>
    fun delete(repair: Repair): Single<Int>
    fun getAll(): Single<List<Repair>>
    fun getById(id: Long): Single<Repair>
    fun getAllForCar(car: Car): Single<List<Repair>>
    fun getAllForPart(part: Part): Single<List<Repair>>
}