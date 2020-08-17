package com.example.mycarsmt.domain.service.note

import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Note
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.Repair
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface NoteService{

    fun create(note: Note): Single<Long>
    fun update(note: Note): Single<Int>
    fun delete(note: Note): Single<Int>
    fun readById(id: Long): Single<Note>
    fun readAll(): Flowable<List<Note>>
    fun readAllForCar(car: Car): Flowable<List<Note>>
    fun readAllForPart(part: Part): Flowable<List<Note>>

    fun getCarFor(note: Note): Maybe<Car>
    fun getPartFor(note: Note): Maybe<Part>
}