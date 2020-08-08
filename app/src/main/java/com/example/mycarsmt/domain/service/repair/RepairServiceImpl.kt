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
class RepairServiceImpl @Inject constructor(): RepairService {

    companion object {
        const val RESULT_REPAIRS_READED = 401
        const val RESULT_REPAIR_READED = 402
        const val RESULT_REPAIR_CREATED = 403
        const val RESULT_REPAIR_UPDATED = 404
        const val RESULT_REPAIR_DELETED = 405
        const val RESULT_REPAIRS_FOR_CAR = 411
        const val RESULT_REPAIRS_FOR_PART = 413
        const val RESULT_REPAIR_CAR = 406
        const val RESULT_REPAIR_PART = 407
    }

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

    override fun create(repair: Repair): Single<Repair> {
        return repairDao.getById(
            repairDao.insert(repairEntityFrom(repair)))
            .map { entity -> repairFrom(entity) }
            .single(repair)
    }

    override fun update(repair: Repair): Single<Int> {
        return Single.just(repairDao.update(repairEntityFrom(repair)))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun delete(repair: Repair): Single<Int> {
        return Single.just(repairDao.delete(repairEntityFrom(repair)))
    }

    override fun readAll(): Flowable<List<Repair>> {
        return repairDao.getAll().map { value ->
            value.stream()
                .map { entity -> repairFrom(entity) }
                .collect(Collectors.toList())
        }
    }

    override fun readById(id: Long): Flowable<Repair> {
        return repairDao.getById(id).map { value -> repairFrom(value) }
    }

    override fun readAllForCar(car: Car): Flowable<List<Repair>> {
        return repairDao.getAllForCar(car.id).map { value ->
            value.stream()
                .map { entity -> repairFrom(entity) }
                .collect(Collectors.toList())
        }
    }

    override fun readAllForPart(part: Part): Flowable<List<Repair>> {
        return repairDao.getAllForCar(part.id).map { value ->
            value.stream()
                .map { entity -> repairFrom(entity) }
                .collect(Collectors.toList())
        }
    }

    override fun getCarFor(repair: Repair): Maybe<Car> {
        return carDao.getById(repair.carId).map { value -> carFrom(value) }.singleElement()
    }

    override fun getPartFor(repair: Repair): Maybe<Part> {
        return partDao.getById(repair.partId).map { value -> partFrom(value) }.singleElement()
    }
}