package com.example.mycarsmt.domain.service.part

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Note
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.Repair
import com.example.mycarsmt.data.database.car.CarDao
import com.example.mycarsmt.data.database.note.NoteDao
import com.example.mycarsmt.data.database.part.PartDao
import com.example.mycarsmt.data.database.repair.RepairDao
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.carFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.noteFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.partEntityFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.partFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.repairEntityFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.repairFrom
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ExecutorService
import java.util.stream.Collectors
import javax.inject.Inject

@SuppressLint("NewApi")
class PartServiceImpl @Inject constructor() : PartService {

    private val TAG = "test_mt"

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

    override fun create(part: Part): Single<Long> {
        Log.d(TAG, "SERVICE: create part")
        return partDao.insert(partEntityFrom(part))
    }

    override fun getById(partId: Long): Single<Part> {
        Log.d(TAG, "SERVICE: readById part")
        return partDao.getById(partId).map { partFrom(it) }
    }

    override fun update(part: Part): Single<Int> {
        Log.d(TAG, "SERVICE: update part")
        part.checkCondition(preferences)
        return partDao.update(partEntityFrom(part))

    }

    override fun delete(part: Part): Single<Int> {
        Log.d(TAG, "SERVICE: create part")
        return partDao.delete(partEntityFrom(part))
    }

    override fun getAll(): Single<List<Part>> {
        Log.d(TAG, "SERVICE: readAll part")
        return partDao.getAll().map { entitiesList ->
            entitiesList.stream()
                .map { entity -> partFrom(entity) }
                .collect(Collectors.toList())
        }
    }

    override fun getAllForCar(car: Car): Single<List<Part>> {
        Log.d(TAG, "SERVICE: all for car part")
        return partDao.getAllForCar(car.id).map { value ->
            value.stream()
                .map { entity -> partFrom(entity) }
                .collect(Collectors.toList())
        }
    }

    override fun refresh(part: Part) {
        partDao.update(partEntityFrom(part))
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun getNotesFor(part: Part): Single<List<Note>> {
        Log.d(TAG, "SERVICE: notes for part")
        return noteDao.getAllForPart(part.id).map { value ->
            value.stream()
                .map { entity -> noteFrom(entity) }
                .collect(Collectors.toList())
        }
    }

    override fun getRepairsFor(part: Part): Single<List<Repair>> {
        Log.d(TAG, "SERVICE: repairs for part")
        return repairDao.getAllForPart(part.id).map { value ->
            value.stream()
                .map { entity -> repairFrom(entity) }
                .collect(Collectors.toList())
        }
    }
}