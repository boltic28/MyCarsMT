package com.example.mycarsmt.datalayer.data.note

import com.example.mycarsmt.businesslayer.Car
import com.example.mycarsmt.businesslayer.Note
import com.example.mycarsmt.businesslayer.Part
import io.reactivex.Maybe
import io.reactivex.Single

interface NoteRepository{

    fun insert(note: Note): Single<Long>

    fun update(note: Note): Single<Int>

    fun delete(note: Note): Single<Int>

    fun getById(id: Long): Single<Note>

    fun getAll(): Single<List<Note>>

    fun getAllForCar(car: Car): Single<List<Note>>

    fun getAllForPart(part: Part): Single<List<Note>>
}