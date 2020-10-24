package com.example.mycarsmt.datalayer.data.part

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.businesslayer.Car
import com.example.mycarsmt.businesslayer.Note
import com.example.mycarsmt.businesslayer.Part
import com.example.mycarsmt.businesslayer.Repair
import com.example.mycarsmt.datalayer.data.car.CarDao
import com.example.mycarsmt.datalayer.data.note.NoteDao
import com.example.mycarsmt.datalayer.data.repair.RepairDao
import com.example.mycarsmt.businesslayer.EntityConverter.Companion.noteFrom
import com.example.mycarsmt.businesslayer.EntityConverter.Companion.partEntityFrom
import com.example.mycarsmt.businesslayer.EntityConverter.Companion.partFrom
import com.example.mycarsmt.businesslayer.EntityConverter.Companion.repairFrom
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ExecutorService
import java.util.stream.Collectors
import javax.inject.Inject

@SuppressLint("NewApi")
class PartRepositoryImpl (private val dao: PartDao) :
    PartRepository {

    override fun insert(part: Part): Single<Long> =
        dao.insert(getEntityFrom(part))

    override fun update(part: Part): Single<Int> =
        dao.update(getEntityFrom(part))

    override fun delete(part: Part): Single<Int> =
        dao.delete(getEntityFrom(part))

    override fun getAll(): Single<List<Part>> =
        dao.getAll().map { listPartWithMileage ->
            listPartWithMileage.map { partWithMileage ->
                getPartFrom(partWithMileage)
            }
        }

    override fun getAllForCar(car: Car): Single<List<Part>> =
        dao.getAllForCar(car.id).map {  listPartWithMileage ->
            listPartWithMileage.map { partWithMileage ->
                getPartFrom(partWithMileage)
            }
        }

    override fun getById(partId: Long): Single<Part> =
        dao.getById(partId).map { partWithMileage ->
            getPartFrom(partWithMileage)
        }
}