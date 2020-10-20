package com.example.mycarsmt.domain.service.note

import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Note
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.Repair
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface NoteRepository{

    fun create(note: Note): Single<Long>
    fun update(note: Note): Single<Int>
    fun delete(note: Note): Single<Int>
    fun getById(id: Long): Single<Note>
    fun getAll(): Single<List<Note>>
    fun getAllForCar(car: Car): Single<List<Note>>
    fun getAllForPart(part: Part): Single<List<Note>>
    fun getCarFor(note: Note): Maybe<Car>
    fun getPartFor(note: Note): Maybe<Part>
}