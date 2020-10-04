package com.example.mycarsmt.domain.service.car

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.mycarsmt.R
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.data.database.car.CarDao
import com.example.mycarsmt.data.database.note.NoteDao
import com.example.mycarsmt.data.database.part.PartDao
import com.example.mycarsmt.data.database.repair.RepairDao
import com.example.mycarsmt.data.enums.PartControlType
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.DiagnosticElement
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.carEntityFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.carFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.noteEntityFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.noteFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.partEntityFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.partFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.repairEntityFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.repairFrom
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ExecutorService
import java.util.stream.Collectors
import javax.inject.Inject

class CarServiceImpl @Inject constructor() : CarService {

    companion object {
        const val TAG = "test_mt"
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

    override fun create(car: Car): Single<Long> {
        Log.d(TAG, "SERVICE: create car")
        return carDao.insert(carEntityFrom(car))
    }

    override fun getById(id: Long): Single<Car> {
        Log.d(TAG, "SERVICE: getSingle car")
        return carDao.getById(id).map { carFrom(it) }
    }

    override fun update(car: Car): Single<Int> {
        Log.d(TAG, "SERVICE: update car")
        car.checkConditions(preferences)
        return carDao.update(carEntityFrom(car))
    }

    override fun delete(car: Car): Single<Int> {
        Log.d(TAG, "SERVICE: delete car")
        return carDao.delete(carEntityFrom(car))
    }

    override fun getAll(): Single<List<Car>> {
        Log.d(TAG, "SERVICE: read all cars")
        return carDao.getAll()
            .map { entitiesList ->
                entitiesList.stream()
                    .map { carEntity -> carFrom(carEntity) }
                    .collect(Collectors.toList())
            }
    }

    override fun preparingCarsToWritingIntoFile(): Single<List<Car>> {
        return Single.fromCallable {
            val cars = carDao.getAllList().stream()
                .map { carEntity -> carFrom(carEntity) }
                .collect(Collectors.toList())

            cars.forEach { car ->
                car.notes = noteDao.getAllForCarList(car.id).stream()
                    .filter { it.partId == 0L }
                    .map { noteFrom(it) }
                    .collect(Collectors.toList())
                car.repairs = repairDao.getAllForCarList(car.id).stream()
                    .filter { it.partId == 0L }
                    .map { repairFrom(it) }
                    .collect(Collectors.toList())
                car.parts = partDao.getAllForCarList(car.id).stream()
                    .map { partFrom(it) }
                    .collect(Collectors.toList())
                car.parts.forEach { part ->
                    part.notes = noteDao.getAllForPartList(part.id).stream()
                        .map { noteFrom(it) }
                        .collect(Collectors.toList())
                    part.repairs = repairDao.getAllForPartList(part.id).stream()
                        .map { repairFrom(it) }
                        .collect(Collectors.toList())
                }
            }

            return@fromCallable cars
        }
    }

    override fun createCarsFromFile(cars: List<Car>): Single<Unit> {
        return Single.fromCallable {
            cars.forEach { car ->
                car.parts.forEach { part ->
                    part.mileage = car.mileage
                    part.checkCondition(preferences)
                }
                car.checkConditions(preferences)

                carDao.insert(carEntityFrom(car))
                    .subscribeOn(Schedulers.io())
                    .subscribe { id ->
                        car.id = id
                        car.notes.forEach { note ->
                            note.carId = id
                            note.partId = 0
                            noteDao.insert(noteEntityFrom(note))
                                .subscribeOn(Schedulers.io())
                                .subscribe()
                        }
                        car.repairs.forEach { repair ->
                            repair.carId = id
                            repair.partId = 0
                            repairDao.insert(repairEntityFrom(repair))
                                .subscribeOn(Schedulers.io())
                                .subscribe()
                        }
                        car.parts.forEach { part ->
                            part.carId = car.id
                            partDao.insert(partEntityFrom(part))
                                .subscribeOn(Schedulers.io())
                                .subscribe { pId ->
                                    part.id = pId
                                    part.notes.forEach { note ->
                                        note.partId = pId
                                        note.carId = id
                                        noteDao.insert(noteEntityFrom(note))
                                            .subscribeOn(Schedulers.io())
                                            .subscribe()
                                    }
                                    part.repairs.forEach { repair ->
                                        repair.partId = pId
                                        repair.carId = id
                                        repairDao.insert(repairEntityFrom(repair))
                                            .subscribeOn(Schedulers.io())
                                            .subscribe()
                                    }
                                }
                        }
                    }
            }
        }
    }

    @SuppressLint("CheckResult")
    override fun getToBuyList(): Single<List<DiagnosticElement>> {
        Log.d(TAG, "SERVICE: get buyList")
        val buyList: MutableList<DiagnosticElement> = mutableListOf()

        carDao.getAll()
            .subscribeOn(Schedulers.computation())
            .map { entity ->
                entity.stream()
                    .map { carFrom(it) }
                    .collect(Collectors.toList())
            }.subscribe { list ->
                list.forEach { car ->
                    partDao.getAllForCar(car.id)
                        .map { value ->
                            value.stream()
                                .map { partFrom(it) }
                                .collect(Collectors.toList())
                        }.subscribe({
                            car.parts = it
                            val element = DiagnosticElement(car, car.getListToBuy())
                            if (element.list.isNotEmpty()) buyList.add(element)
                        }, {
                            Log.d(TAG, "SERVICE: buyList error")
                        })
                }

            }

        return Single.just(buyList)
    }

    @SuppressLint("CheckResult")
    override fun getToDoList(): Single<List<DiagnosticElement>> {
        Log.d(TAG, "SERVICE: get toDoList")
        val todoList: MutableList<DiagnosticElement> = mutableListOf()

        carDao.getAll()
            .subscribeOn(Schedulers.computation())
            .map { entity ->
                entity.stream()
                    .map { carFrom(it) }
                    .collect(Collectors.toList())
            }.subscribe { list ->
                list.forEach { car ->
                    partDao.getAllForCar(car.id)
                        .map { value ->
                            value.stream()
                                .map { partFrom(it) }
                                .collect(Collectors.toList())
                        }.subscribe({
                            car.parts = it
                            val element = DiagnosticElement(car, car.getListToDo())
                            if (element.list.isNotEmpty()) todoList.add(element)
                        }, {
                            Log.d(TAG, "SERVICE: todoList error")
                        })
                }

            }
        return Single.just(todoList)
    }

    @SuppressLint("CheckResult")
    override fun refreshAll(): Single<Unit> {
        return Single.fromCallable {

            carDao.getAllList().stream()
                .map { carEntity -> carFrom(carEntity) }
                .collect(Collectors.toList())
                .forEach { refresh(it) }
        }
    }

    @SuppressLint("CheckResult")
    override fun refresh(car: Car) {
        partDao.getAllForCar(car.id)
            .map { entityPartsList ->
                entityPartsList.stream()
                    .map { partFrom(it) }
                    .collect(Collectors.toList())
            }
            .subscribeOn(Schedulers.io())
            .subscribe({
                car.parts = it
                it.forEach { part ->
                    part.checkCondition(preferences)
                    partDao.update(partEntityFrom(part))
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                }
                car.checkConditions(preferences)
                carDao.update(carEntityFrom(car)).subscribe()
            }, { err ->
                Log.d(TAG, "SERVICE: refresh error: $err")
            })
    }

    override fun makeDiagnosticForNotification() {
//        val handlerThread = HandlerThread("diagnosticThread")
//        handlerThread.start()
//        val looper = handlerThread.looper
//        val handler = Handler(looper)
//        handler.post {
        //            val cars = carDao.getAll().stream()
//                .map { entity -> carFrom(entity) }
//                .map { car -> makeDiagnosticAndSave(car) }
//                .filter { !it.condition.contains(Condition.OK) }
//                .collect(Collectors.toList())
//
//            val line = "${cars.size} has problem, let's see it!"

//            mainHandler.sendMessage(handler.obtainMessage(RESULT_DIAGNOSTIC_FOR_NOTIFICATION, line))
//            handlerThread.quit()
//        }
    }

    @SuppressLint("CheckResult")
    override fun createCommonPartsFor(car: Car, context: Context): Single<Int> {
        val list = mutableListOf<Part>()

        list.add(
            Part(
                car.id,
                car.mileage,
                context.resources.getString(R.string.common_part_oil_level),
                5000,
                15,
                PartControlType.INSPECTION
            )
        )
        list.add(
            Part(
                car.id,
                car.mileage,
                context.resources.getString(R.string.common_part_oil_filter),
                15000,
                365,
                PartControlType.CHANGE
            )
        )
        list.add(
            Part(
                car.id,
                car.mileage,
                context.resources.getString(R.string.common_part_air_filter),
                30000,
                365,
                PartControlType.CHANGE
            )
        )
        list.add(
            Part(
                car.id,
                car.mileage,
                context.resources.getString(R.string.common_part_cabin_filter),
                30000,
                365,
                PartControlType.CHANGE
            )
        )
        list.add(
            Part(
                car.id,
                car.mileage,
                context.resources.getString(R.string.common_part_spark_plugs),
                30000,
                0,
                PartControlType.CHANGE
            )
        )
        list.add(
            Part(
                car.id,
                car.mileage,
                context.resources.getString(R.string.common_part_front_brake),
                40000,
                0,
                PartControlType.INSPECTION
            )
        )
        list.add(
            Part(
                car.id,
                car.mileage,
                context.resources.getString(R.string.common_part_rear_brake),
                40000,
                0,
                PartControlType.INSPECTION
            )
        )
        list.add(
            Part(
                car.id,
                car.mileage,
                context.resources.getString(R.string.common_part_wipers),
                0,
                183,
                PartControlType.INSPECTION
            )
        )
        list.add(
            Part(
                car.id,
                car.mileage,
                context.resources.getString(R.string.common_part_insurance),
                0,
                365,
                PartControlType.INSURANCE
            )
        )
        list.add(
            Part(
                car.id,
                car.mileage,
                context.resources.getString(R.string.common_part_techview),
                0,
                365,
                PartControlType.INSURANCE
            )
        )

        list.forEach { part ->
            partDao.insert(partEntityFrom(part))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        }

        refresh(car)

        return Single.just(1)
    }
}

