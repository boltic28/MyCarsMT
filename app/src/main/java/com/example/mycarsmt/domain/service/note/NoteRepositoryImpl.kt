package com.example.mycarsmt.domain.service.note

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.datalayer.data.car.CarDao
import com.example.mycarsmt.datalayer.data.note.NoteDao
import com.example.mycarsmt.datalayer.data.part.PartDao
import com.example.mycarsmt.datalayer.data.repair.RepairDao
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Note
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.carFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.noteEntityFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.noteFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.partFrom
import io.reactivex.Maybe
import io.reactivex.Single
import java.util.concurrent.ExecutorService
import java.util.stream.Collectors
import javax.inject.Inject

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

    override fun getCarFor(note: Note): Maybe<Car> {
        TODO("Not yet implemented")
    }

    override fun getPartFor(note: Note): Maybe<Part> {
        TODO("Not yet implemented")
    }
}