package com.example.mycarsmt.datalayer.data.repair

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.businesslayer.Repair
import com.example.mycarsmt.datalayer.data.car.CarDao
import com.example.mycarsmt.datalayer.data.part.PartDao
import com.example.mycarsmt.datalayer.data.repair.RepairDao
import com.example.mycarsmt.businesslayer.Car
import com.example.mycarsmt.businesslayer.Part
import com.example.mycarsmt.businesslayer.service.mappers.EntityConverter.Companion.carFrom
import com.example.mycarsmt.businesslayer.service.mappers.EntityConverter.Companion.partFrom
import com.example.mycarsmt.businesslayer.service.mappers.EntityConverter.Companion.repairEntityFrom
import com.example.mycarsmt.businesslayer.service.mappers.EntityConverter.Companion.repairFrom
import com.example.mycarsmt.datalayer.data.repair.RepairRepository
import io.reactivex.Maybe
import io.reactivex.Single
import java.util.concurrent.ExecutorService
import java.util.stream.Collectors
import javax.inject.Inject

@SuppressLint("NewApi")
class RepairRepositoryImpl(private val dao: RepairDao) :
    RepairRepository {

    override fun insert(repair: Repair): Single<Long> =
        dao.insert(getEntityFrom(repair))

    override fun update(repair: Repair): Single<Int> =
        dao.update(getEntityFrom(repair))

    override fun delete(repair: Repair): Single<Int> =
        dao.delete(getEntityFrom(repair))

    override fun getAll(): Single<List<Repair>> =
        dao.getAll().map { listRepairEntity ->
            listRepairEntity.map { entity ->
                getRepairFrom(entity)
            }
        }

    override fun getById(id: Long): Single<Repair> =
        dao.getById(id).map { entity ->
            getRepairFrom(entity)
        }

    override fun getAllForCar(car: Car): Single<List<Repair>> =
        dao.getAllForCar(car.id).map { listRepairEntity ->
            listRepairEntity.map { entity ->
                getRepairFrom(entity)
            }
        }

    override fun getAllForPart(part: Part): Single<List<Repair>> =
        dao.getAllForPart(part.id).map { listRepairEntity ->
            listRepairEntity.map { entity ->
                getRepairFrom(entity)
            }
        }
}