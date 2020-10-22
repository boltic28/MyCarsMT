package com.example.mycarsmt.businesslayer.service.note

import android.annotation.SuppressLint
import com.example.mycarsmt.datalayer.data.note.NoteDao
import com.example.mycarsmt.businesslayer.Car
import com.example.mycarsmt.businesslayer.Note
import com.example.mycarsmt.businesslayer.Part
import com.example.mycarsmt.datalayer.data.note.NoteEntity
import com.example.mycarsmt.datalayer.data.note.NoteWithMileage
import io.reactivex.Maybe
import io.reactivex.Single

@SuppressLint("NewApi")
class NoteRepositoryImpl(private val noteDao: NoteDao) : NoteRepository {

    override fun create(note: Note): Single<Long> {
        TODO("Not yet implemented")
    }

    override fun update(note: Note): Single<Int> {
        TODO("Not yet implemented")
    }

    override fun delete(note: Note): Single<Int> {
        TODO("Not yet implemented")
    }

    override fun getById(id: Long): Single<Note> {
        TODO("Not yet implemented")
    }

    override fun getAll(): Single<List<Note>> {
        TODO("Not yet implemented")
    }

    override fun getAllForCar(car: Car): Single<List<Note>> {
        TODO("Not yet implemented")
    }

    override fun getAllForPart(part: Part): Single<List<Note>> {
        TODO("Not yet implemented")
    }
}

fun getNoteFrom(entity: NoteWithMileage): Note{}

fun getEntityFrom(note: Note): NoteEntity{}