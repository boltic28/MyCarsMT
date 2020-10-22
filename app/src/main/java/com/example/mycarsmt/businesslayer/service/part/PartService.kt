package com.example.mycarsmt.businesslayer.service.part

import com.example.mycarsmt.businesslayer.Car
import com.example.mycarsmt.businesslayer.Note
import com.example.mycarsmt.businesslayer.Part
import com.example.mycarsmt.businesslayer.Repair
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