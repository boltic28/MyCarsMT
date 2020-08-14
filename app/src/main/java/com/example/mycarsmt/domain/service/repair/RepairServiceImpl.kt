package com.example.mycarsmt.domain.service.repair

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.domain.Repair
import com.example.mycarsmt.data.database.car.CarDao
import com.example.mycarsmt.data.database.part.PartDao
import com.example.mycarsmt.data.database.repair.RepairDao
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.carFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.partFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.repairEntityFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.repairFrom
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ExecutorService
import java.util.stream.Collectors
import javax.inject.Inject

@SuppressLint("NewApi")
class RepairServiceImpl @Inject constructor() : RepairService {

    private val TAG = "testmt"

    @Inject
    lateinit var carDao: CarDao
    @Inject
    lateinit var partDao: PartDao
    @Inject
    lateinit var repairDao: RepairDao
    @Inject
    lateinit var preferences: SharedPreferences
    @Inject
    lateinit var executor: ExecutorService

    init {
        App.component.injectService(this)
    }

    override fun create(repair: Repair): Single<Long> {
        return repairDao.insert(repairEntityFrom(repair))
    }

    override fun readById(id: Long): Single<Repair> {
        return repairDao.getById(id).map { repairFrom(it) }
    }

    override fun update(repair: Repair): Single<Int> {
        return repairDao.update(repairEntityFrom(repair))
    }

    override fun delete(repair: Repair): Single<Int> {
        return repairDao.delete(repairEntityFrom(repair))
    }

    override fun readAll(): Flowable<List<Repair>> {
        return repairDao.getAll().map { entitiesList ->
            entitiesList.stream()
                .map { entity -> repairFrom(entity) }
                .collect(Collectors.toList())
        }
    }

    override fun readAllForCar(car: Car): Flowable<List<Repair>> {
        return repairDao.getAllForCar(car.id).map { entitiesList ->
            entitiesList.stream()
                .map { entity -> repairFrom(entity) }
                .collect(Collectors.toList())
        }
    }

    override fun readAllForPart(part: Part): Flowable<List<Repair>> {
        return repairDao.getAllForCar(part.id).map { entitiesList ->
            entitiesList.stream()
                .map { entity -> repairFrom(entity) }
                .collect(Collectors.toList())
        }
    }

    override fun getCarFor(repair: Repair): Maybe<Car> {
        return carDao.getById(repair.carId).map { value -> carFrom(value) }.toMaybe()
    }

    override fun getPartFor(repair: Repair): Maybe<Part> {
        return partDao.getById(repair.partId).map { value -> partFrom(value) }.toMaybe()
    }
}