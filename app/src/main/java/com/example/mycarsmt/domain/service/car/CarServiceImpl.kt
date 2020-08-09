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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.time.LocalDate
import java.util.concurrent.ExecutorService
import java.util.stream.Collectors
import javax.inject.Inject

class CarServiceImpl @Inject constructor() : CarService {

    companion object {
        const val TAG = "testmt"
        const val RESULT_CARS_READED = 101
        const val RESULT_CAR_READED = 102
        const val RESULT_CAR_CREATED = 103
        const val RESULT_CAR_UPDATED = 104
        const val RESULT_CAR_DELETED = 105
        const val RESULT_PARTS_FOR_CAR = 111
        const val RESULT_NOTES_FOR_CAR = 112
        const val RESULT_REPAIRS_FOR_CAR = 113
        const val RESULT_BUY_LIST = 114
        const val RESULT_TO_DO_LIST = 115
        const val DIAGNOSTIC_IS_READY = 120
        const val DIAGNOSTIC_CAR_IS_READY = 121
        const val RESULT_DIAGNOSTIC_FOR_NOTIFICATION = 122
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

    override fun create(car: Car): Single<Car> {
        return carDao.getById(carDao.insert(carEntityFrom(car)))
            .map { entity -> carFrom(entity) }
            .single(car)
    }

    override fun update(car: Car): Single<Int> {
        return Single.just(carDao.update(carEntityFrom(car)))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun delete(car: Car): Single<Int> {
        return Single.just(carDao.delete(carEntityFrom(car)))
    }

    override fun readAll(): Flowable<List<Car>> {
        return carDao.getAll()
            .map { entity ->
                entity.stream()
                    .map { carEntity -> carFrom(carEntity) }
                    .collect(Collectors.toList())
            }
    }

    override fun readById(id: Long): Flowable<Car> {
        return carDao.getById(id).map { entity -> carFrom(entity) }
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
    override fun getToBuyList(): Flowable<List<DiagnosticElement>> {
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

        return Flowable.fromArray(buyList)
    }

    @SuppressLint("CheckResult")
    override fun getToDoList(): Flowable<List<DiagnosticElement>> {
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

        return Flowable.fromArray(todoList)
    }

    @SuppressLint("CheckResult")
    override fun doDiagnosticAllCars() {
        carDao.getAll()
//            .subscribeOn(Schedulers.computation())
            .map { value ->
                value.stream()
                    .map { entity -> carFrom(entity) }
                    .collect(Collectors.toList())
            }
//            .observeOn(AndroidSchedulers.mainThread())
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

    // for delete(only for test show)
//    fun createTestEntityForApp() {
//        executor.execute {
//            Runnable {
//                val car = Car()
//                car.brand = "Kia"
//                car.model = "Rio"
//                car.number = "1452 HA-4"
//                car.vin = "56718394GHDJY567"
//                car.year = 2013
//                car.mileage = 43000
//                car.whenMileageRefreshed = LocalDate.of(2020, 5, 24)
//                car.mileage = 56500
//                carDao.insert(carEntityFrom(car))
//
//                val car1 = Car()
//                car1.brand = "Kia"
//                car1.model = "Soul"
//                car1.number = "1342 TA-6"
//                car1.vin = "5671WER34HDJY567"
//                car1.year = 2016
//                car1.mileage = 23000
//                car1.whenMileageRefreshed = LocalDate.of(2020, 4, 14)
//                car1.mileage = 27600
//                carDao.insert(carEntityFrom(car1))
//            }.run()
//        }
//    }
}

