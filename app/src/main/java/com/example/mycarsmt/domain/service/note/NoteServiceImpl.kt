package com.example.mycarsmt.domain.service.note

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.domain.Note
import com.example.mycarsmt.domain.Repair
import com.example.mycarsmt.data.database.car.CarDao
import com.example.mycarsmt.data.database.note.NoteDao
import com.example.mycarsmt.data.database.part.PartDao
import com.example.mycarsmt.data.database.repair.RepairDao
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.carFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.noteEntityFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.noteFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.partFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.repairEntityFrom
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ExecutorService
import java.util.stream.Collectors
import javax.inject.Inject

@SuppressLint("NewApi")
class NoteServiceImpl @Inject constructor() : NoteService {

    @Inject
    lateinit var carDao: CarDao
    @Inject
    lateinit var partDao: PartDao
    @Inject
    lateinit var noteDao: NoteDao
    @Inject
    lateinit var repairDao: RepairDao
    @Inject
    lateinit var preferences: SharedPreferences
    @Inject
    lateinit var executor: ExecutorService

    init {
        App.component.injectService(this)
    }

    override fun readAll(): Flowable<List<Note>> {
        return noteDao.getAll().map { value ->
            value.stream()
                .map { entity -> noteFrom(entity) }
                .collect(Collectors.toList()) }
    }

    override fun readById(id: Long): Flowable<Note> {
        return noteDao.getById(id).map { value -> noteFrom(value) }
    }

    override fun readAllForCar(car: Car): Flowable<List<Note>> {
        return noteDao.getAllForCar(car.id).map { value ->
            value.stream()
                .map { entity -> noteFrom(entity) }
                .collect(Collectors.toList())}
    }

    override fun readAllForPart(part: Part): Flowable<List<Note>> {
        return noteDao.getAllForPart(part.id).map { value ->
            value.stream()
                .map { entity -> noteFrom(entity) }
                .collect(Collectors.toList())
        }
    }

    override fun create(note: Note): Single<Note> {
        return noteDao.getById(
            noteDao.insert(noteEntityFrom(note)))
            .map { entity -> noteFrom(entity) }
            .single(note)
    }

    override fun update(note: Note): Single<Int> {
        return Single.just(noteDao.update(noteEntityFrom(note)))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun delete(note: Note): Single<Int> {
        return Single.just(noteDao.delete(noteEntityFrom(note)))
    }

    override fun getCarFor(note: Note): Maybe<Car> {
        return carDao.getById(note.carId)
            .map { entity -> carFrom(entity) }
            .singleElement()
    }

    override fun getPartFor(note: Note): Maybe<Part> {
        return partDao.getById(note.partId)
            .map { value -> partFrom(value) }
            .singleElement()
    }

    override fun addRepair(repair: Repair) {
        executor.execute{
            Runnable { repairDao.insert(repairEntityFrom(repair)) }
        }
    }
}