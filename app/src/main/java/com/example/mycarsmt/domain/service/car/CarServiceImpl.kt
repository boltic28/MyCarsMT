package com.example.mycarsmt.domain.service.car

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.data.database.car.CarDao
import com.example.mycarsmt.data.database.part.PartDao
import com.example.mycarsmt.data.enums.PartControlType
import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.DiagnosticElement
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.carEntityFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.carFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.partEntityFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.partFrom
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
    override fun refreshAll() {
        carDao.getAll()
            .subscribeOn(Schedulers.io())
            .map { value ->
                value.stream()
                    .map { carFrom(it) }
                    .collect(Collectors.toList())
            }
            .subscribe(
                { list -> list.forEach { refresh(it) } },
                { err -> Log.d(TAG, "ERROR: doDiagnosticForAllCars -> writing in DB: $err") }
            )
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
            .subscribe( {
                car.parts = it
                it.forEach { part ->
                    part.checkCondition(preferences)
                    partDao.update(partEntityFrom(part))
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                }
                car.checkConditions(preferences)
                carDao.update(carEntityFrom(car)).subscribe()
            },{
                    err -> Log.d(TAG, "SERVICE: refresh error: $err")
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

