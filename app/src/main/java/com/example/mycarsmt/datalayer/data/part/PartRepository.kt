package com.example.mycarsmt.datalayer.data.part

import com.example.mycarsmt.businesslayer.Car
import com.example.mycarsmt.businesslayer.Note
import com.example.mycarsmt.businesslayer.Part
import com.example.mycarsmt.businesslayer.Repair
import io.reactivex.Single

interface PartRepository{

    fun insert(part: Part): Single<Long>

    fun update(part: Part): Single<Int>

    fun delete(part: Part): Single<Int>

    fun getAll(): Single<List<Part>>

    fun getAllForCar(car: Car): Single<List<Part>>

    fun getById(partId: Long): Single<Part>
}