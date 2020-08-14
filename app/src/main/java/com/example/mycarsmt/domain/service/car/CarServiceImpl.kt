package com.example.mycarsmt.domain.service.car

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.data.database.car.CarDao
import com.example.mycarsmt.data.database.note.NoteDao
import com.example.mycarsmt.data.database.part.PartDao
import com.example.mycarsmt.data.database.repair.RepairDao
import com.example.mycarsmt.data.enums.PartControlType
import com.example.mycarsmt.domain.*
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.carEntityFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.carFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.noteFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.partEntityFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.partFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.repairFrom
import io.reactivex.Flowable
import io.reactivex.Single
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
        return carDao.insert(carEntityFrom(car))
    }

    override fun readById(id: Long): Single<Car> {
        return carDao.getById(id).map { carFrom(it) }
    }

    override fun update(car: Car): Single<Int> {
        return carDao.update(carEntityFrom(car))
    }

    override fun delete(car: Car): Single<Int> {
        return carDao.delete(carEntityFrom(car))
    }

    override fun readAll(): Flowable<List<Car>> {
        return carDao.getAll()
            .map { entitiesList ->
                entitiesList.stream()
                    .map { carEntity -> carFrom(carEntity) }
                    .collect(Collectors.toList())
            }
    }

    override fun getPartsFor(car: Car): Flowable<List<Part>> {
        return partDao.getAllForCar(car.id).map { value ->
            value.stream()
                .map { entity -> partFrom(entity) }
                .collect(Collectors.toList())
        }
    }

    override fun getNotesFor(car: Car): Flowable<List<Note>> {
        return noteDao.getAllForCar(car.id).map { value ->
            value.stream()
                .map { entity -> noteFrom(entity) }
                .collect(Collectors.toList())
        }
    }

    override fun getRepairsFor(car: Car): Flowable<List<Repair>> {
        return repairDao.getAllForCar(car.id).map { value ->
            value.stream()
                .map { entity -> repairFrom(entity) }
                .collect(Collectors.toList())
        }
    }

    @SuppressLint("CheckResult")
    override fun getToBuyList(): Single<List<DiagnosticElement>> {
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
                        }.subscribe {
                            car.parts = it
                            val element = DiagnosticElement(car, car.getListToBuy())
                            if (element.list.isNotEmpty()) buyList.add(element)
                        }
                }

            }

        return Single.just(buyList)
    }

    @SuppressLint("CheckResult")
    override fun getToDoList(): Single<List<DiagnosticElement>> {
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
                        }.subscribe {
                            car.parts = it
                            val element = DiagnosticElement(car, car.getListToDo())
                            if (element.list.isNotEmpty()) todoList.add(element)
                        }
                }

            }

        return Single.just(todoList)
    }

    @SuppressLint("CheckResult")
    override fun doDiagnosticAllCars() {
        carDao.getAll()
            .map { value ->
                value.stream()
                    .map { entity -> carFrom(entity) }
                    .collect(Collectors.toList())
            }
            .subscribe(
                { list -> list.forEach { refresh(it) } },
                { err -> Log.d(TAG, "ERROR: doDiagnosticForAllCars -> writing in DB: $err") }
            )
    }

    @SuppressLint("CheckResult")
    override fun doDiagnosticForCar(car: Car) {
        carDao.getById(car.id)
            .subscribeOn(Schedulers.computation())
            .map { entity -> carFrom(entity) }
            .subscribe(
                { refresh(it) },
                { err -> Log.d(TAG, "ERROR: doDiagnosticForCar -> writing in DB: $err") }
            )
    }

    @SuppressLint("CheckResult")
    override fun refresh(car: Car) {
        executor.execute {
            Runnable {
                partDao.getAllForCar(car.id)
                    .map { list ->
                        list.stream()
                            .map { entity -> partFrom(entity) }
                            .collect(Collectors.toList())
                    }.subscribe { list ->
                        list.forEach { part -> part.checkCondition(preferences) }
                        car.parts = list
                        car.checkConditions(preferences)
                        carDao.update(carEntityFrom(car))
                    }
            }
        }
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

    override fun createCommonPartsFor(car: Car) {
        executor.execute {
            Runnable {
                val list = mutableListOf<Part>()

                list.add(
                    Part(
                        car.id,
                        car.mileage,
                        "Oil level",
                        5000,
                        15,
                        PartControlType.INSPECTION
                    )
                )
                list.add(
                    Part(
                        car.id,
                        car.mileage,
                        "Oil filter",
                        15000,
                        365,
                        PartControlType.CHANGE
                    )
                )
                list.add(
                    Part(
                        car.id,
                        car.mileage,
                        "Air filter",
                        30000,
                        365,
                        PartControlType.CHANGE
                    )
                )
                list.add(
                    Part(
                        car.id,
                        car.mileage,
                        "Cabin filter",
                        30000,
                        365,
                        PartControlType.CHANGE
                    )
                )
                list.add(
                    Part(
                        car.id,
                        car.mileage,
                        "Spark plugs",
                        30000,
                        0,
                        PartControlType.CHANGE
                    )
                )
                list.add(
                    Part(
                        car.id,
                        car.mileage,
                        "Front brake",
                        40000,
                        0,
                        PartControlType.INSPECTION
                    )
                )
                list.add(
                    Part(
                        car.id,
                        car.mileage,
                        "Rear brake",
                        40000,
                        0,
                        PartControlType.INSPECTION
                    )
                )
                list.add(
                    Part(
                        car.id,
                        car.mileage,
                        "Wipers",
                        0,
                        183,
                        PartControlType.INSPECTION
                    )
                )
                list.add(
                    Part(
                        car.id,
                        car.mileage,
                        "Insurance",
                        0,
                        365,
                        PartControlType.INSURANCE
                    )
                )
                list.add(
                    Part(
                        car.id,
                        car.mileage,
                        "Tech view",
                        0,
                        365,
                        PartControlType.INSURANCE
                    )
                )

                list.forEach { part ->
                    part.checkCondition(preferences)
                    partDao.insert(partEntityFrom(part))
                }

                car.parts = list
                car.checkConditions(preferences)
                carDao.update(carEntityFrom(car))
            }.run()
        }
    }
}

