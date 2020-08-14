package com.example.mycarsmt.domain.service.part

import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Note
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.Repair
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface PartService{

    fun create(part: Part): Single<Long>
    fun update(part: Part): Single<Int>
    fun delete(part: Part): Single<Int>
    fun readAll(): Flowable<List<Part>>
    fun readAllForCar(car: Car): Flowable<List<Part>>
    fun readById(partId: Long): Single<Part>

    fun getNotesFor(part: Part): Flowable<List<Note>>
    fun getRepairsFor(part: Part): Flowable<List<Repair>>

    fun getCarFor(part: Part): Maybe<Car>
    fun addRepair(repair: Repair)
}