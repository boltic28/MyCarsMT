package com.example.mycarsmt.datalayer.data.note

import android.annotation.SuppressLint
import com.example.mycarsmt.businesslayer.Car
import com.example.mycarsmt.businesslayer.Note
import com.example.mycarsmt.businesslayer.Part
import io.reactivex.Single

@SuppressLint("NewApi")
class NoteRepositoryImpl(private val noteDao: NoteDao) :
    NoteRepository {

    override fun insert(note: Note): Single<Long> =
        noteDao.insert(getEntityFrom(note))

    override fun update(note: Note): Single<Int> =
        noteDao.update(getEntityFrom(note))

    override fun delete(note: Note): Single<Int> =
        noteDao.delete(getEntityFrom(note))

    override fun getById(id: Long): Single<Note> =
        noteDao.getById(id).map { noteEntity ->
            getNoteFromEntity(noteEntity)
        }

    override fun getAll(): Single<List<Note>> =
        noteDao.getAll().map {listEntity ->
            listEntity.map { noteEntity ->
                getNoteFromEntity(noteEntity)
            }
        }

    override fun getAllForCar(car: Car): Single<List<Note>> =
        noteDao.getAllForCar(car.id).map { listEntity ->
            listEntity.map {noteEntity ->
                getNoteFromEntity(noteEntity)
            }
        }

    override fun getAllForPart(part: Part): Single<List<Note>> =
        noteDao.getAllForPart(part.id).map { listEntity ->
            listEntity.map { noteEntity ->
                getNoteFromEntity(noteEntity)
            }
        }
}

