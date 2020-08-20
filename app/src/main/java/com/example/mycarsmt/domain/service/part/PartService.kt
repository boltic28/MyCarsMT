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
    fun getAll(): Single<List<Part>>
    fun getAllForCar(car: Car): Single<List<Part>>
    fun getById(partId: Long): Single<Part>

    fun refresh(part: Part)

    fun getNotesFor(part: Part): Single<List<Note>>
    fun getRepairsFor(part: Part): Single<List<Repair>>
}