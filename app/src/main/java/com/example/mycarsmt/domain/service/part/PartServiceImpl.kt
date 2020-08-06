package com.example.mycarsmt.domain.service.part

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.HandlerThread
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Note
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.Repair
import com.example.mycarsmt.data.AppDatabase
import com.example.mycarsmt.data.database.car.CarDao
import com.example.mycarsmt.data.database.note.NoteDao
import com.example.mycarsmt.data.database.part.PartDao
import com.example.mycarsmt.data.database.repair.RepairDao
import com.example.mycarsmt.domain.service.mappers.EntityConverter
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.carEntityFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.carFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.noteFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.partEntityFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.partFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.repairEntityFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.repairFrom
import com.example.mycarsmt.presentation.fragments.SettingFragment
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import java.util.concurrent.ExecutorService
import java.util.stream.Collectors
import javax.inject.Inject

@SuppressLint("NewApi")
class PartServiceImpl @Inject constructor() : PartService {

    companion object {
        const val TAG = "testmt"

        const val RESULT_PARTS_READ = 201
        const val RESULT_PART_READ = 202
        const val RESULT_PART_CREATED = 203
        const val RESULT_PART_UPDATED = 204
        const val RESULT_PART_DELETED = 205
        const val RESULT_PART_CAR = 206
        const val RESULT_PARTS_FOR_CAR = 211
        const val RESULT_NOTES_FOR_PART = 212
        const val RESULT_REPAIRS_FOR_PART = 213
    }

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

    override fun create(part: Part): Single<Part> {
        return partDao.getById(
            partDao.insert(partEntityFrom(part)))
            .map { entity -> partFrom(entity) }
            .single(part)
    }

    override fun update(part: Part): Single<Part> {
        executor.execute {
            Runnable { partDao.update(partEntityFrom(part)) }.run()
        }
        return Single.just(part)
    }

    override fun delete(part: Part): Single<Int> {
        return Single.just(partDao.delete(partEntityFrom(part)))
    }

    override fun readAll(): Flowable<List<Part>> {
        return partDao.getAll().map { value ->
            value.stream()
                .map { entity -> partFrom(entity) }
                .collect(Collectors.toList())
        }
    }

    override fun readAllForCar(car: Car): Flowable<List<Part>> {
        return partDao.getAllForCar(car.id).map { value ->
            value.stream()
                .map { entity -> partFrom(entity) }
                .collect(Collectors.toList())
        }
    }

    override fun readById(partId: Long): Flowable<Part> {
        return partDao.getById(partId).map { value -> partFrom(value) }
    }

    override fun getNotesFor(part: Part): Flowable<List<Note>> {
        return noteDao.getAllForPart(part.id).map { value ->
            value.stream()
                .map { entity -> noteFrom(entity) }
                .collect(Collectors.toList())
        }
    }

    override fun getRepairsFor(part: Part): Flowable<List<Repair>> {
        return repairDao.getAllForPart(part.id).map { value ->
            value.stream()
                .map { entity -> repairFrom(entity) }
                .collect(Collectors.toList())
        }
    }

    override fun getCarFor(part: Part): Maybe<Car> {
        return carDao.getById(part.carId).map { value -> carFrom(value) }.singleElement()
    }

    override fun addRepair(repair: Repair) {
        executor.execute{
            Runnable { repairDao.insert(repairEntityFrom(repair)) }
        }
    }
}