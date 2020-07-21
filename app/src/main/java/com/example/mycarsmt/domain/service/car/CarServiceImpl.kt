package com.example.mycarsmt.domain.service.car

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.HandlerThread
import com.example.mycarsmt.data.database.AppDatabase
import com.example.mycarsmt.data.database.car.CarDao
import com.example.mycarsmt.data.database.note.NoteDao
import com.example.mycarsmt.data.database.part.PartDao
import com.example.mycarsmt.data.database.repair.RepairDao
import com.example.mycarsmt.data.enums.Condition
import com.example.mycarsmt.data.enums.PartControlType
import com.example.mycarsmt.domain.*
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.carEntityFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.carFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.noteFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.partEntityFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.partFrom
import com.example.mycarsmt.domain.service.mappers.EntityConverter.Companion.repairFrom
import com.example.mycarsmt.presentation.fragments.SettingFragment
import java.time.LocalDate
import java.util.stream.Collectors

class CarServiceImpl(context: Context, handler: Handler) : CarService {

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

    private var mainHandler: Handler
    private var carDao: CarDao
    private var partDao: PartDao
    private var noteDao: NoteDao
    private var repairDao: RepairDao
    private var preferences: SharedPreferences

    private lateinit var cars: List<Car>

    init {
        val db: AppDatabase = AppDatabase.getInstance(context)!!
        mainHandler = handler
        carDao = db.carDao()
        partDao = db.partDao()
        noteDao = db.noteDao()
        repairDao = db.repairDao()
        preferences = context.getSharedPreferences(
            SettingFragment.APP_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    override fun readAll() {
        val handlerThread = HandlerThread("readThread")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        handler.post {
            cars = carDao.getAll().stream().map { carEntity -> carFrom(carEntity) }
                .collect(Collectors.toList())

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_CARS_READED, cars))
            handlerThread.quit()
        }
    }

    override fun readById(id: Long) {
        var car: Car
        val handlerThread = HandlerThread("readThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            car = carFrom(carDao.getById(id))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_CAR_READED, car))
            handlerThread.quit()
        }
    }

    override fun create(car: Car) {
        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            car.id = carDao.insert(carEntityFrom(car))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_CAR_CREATED, car))
            handlerThread.quit()
        }
    }

    override fun update(car: Car) {
        val handlerThread = HandlerThread("updateThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            val carResult = makeDiagnosticAndSave(car)

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_CAR_UPDATED, carResult))
            handlerThread.quit()
        }
    }

    override fun delete(car: Car) {
        val handlerThread = HandlerThread("deleteThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            carDao.delete(carEntityFrom(car))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_CAR_DELETED))
            handlerThread.quit()
        }
    }

    override fun getPartsFor(car: Car) {
        var parts: List<Part>
        val handlerThread = HandlerThread("readPartForCarThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            parts = partDao.getAllForCar(car.id).stream().map { entity -> partFrom(entity) }
                .collect(Collectors.toList())

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_PARTS_FOR_CAR, parts))
            handlerThread.quit()
        }
    }

    override fun getNotesFor(car: Car) {
        var notes: List<Note>
        val handlerThread = HandlerThread("readNoteForCarThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            notes = noteDao.getAllForCar(car.id).stream()
                .map { entity -> noteFrom(entity) }
                .collect(Collectors.toList())

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_NOTES_FOR_CAR, notes))
            handlerThread.quit()
        }
    }

    override fun getRepairsFor(car: Car) {
        var repairs: List<Repair>
        val handlerThread = HandlerThread("readRepairForCarThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            repairs = repairDao.getAllForCar(car.id).stream()
                .map { entity -> repairFrom(entity) }
                .collect(Collectors.toList())

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_REPAIRS_FOR_CAR, repairs))
            handlerThread.quit()
        }
    }

    override fun getToBuyList() {
        val handlerThread = HandlerThread("preparingBuyListThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            val buyList = mutableListOf<DiagnosticElement>()
            carDao.getAll().stream()
                .map { entity -> carFrom(entity) }
                .collect(Collectors.toList())
                .forEach { car ->
                    car.parts = partDao.getAllForCar(car.id).stream()
                        .map { entity -> partFrom(entity) }
                        .collect(Collectors.toList())

                    val element = DiagnosticElement(
                        car,
                        car.getListToBuy()
                    )

                    if (element.list.isNotEmpty()) {
                        buyList.add(
                            DiagnosticElement(
                                car,
                                car.getListToBuy()
                            )
                        )
                    }
                }

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_BUY_LIST, buyList))
            handlerThread.quit()
        }
    }

    override fun getToDoList() {
        val handlerThread = HandlerThread("preparingToDoListThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            val toDoList = mutableListOf<DiagnosticElement>()
            carDao.getAll().stream()
                .map { entity -> carFrom(entity) }
                .collect(Collectors.toList())
                .forEach { car ->
                    car.parts = partDao.getAllForCar(car.id).stream()
                        .map { entity -> partFrom(entity) }
                        .collect(Collectors.toList())

                    val element = DiagnosticElement(
                        car,
                        car.getListToBuy()
                    )

                    if (element.list.isNotEmpty()) {
                        toDoList.add(
                            DiagnosticElement(
                                car,
                                car.getListToDo()
                            )
                        )
                    }
                }

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_TO_DO_LIST, toDoList))
            handlerThread.quit()
        }
    }

    override fun doDiagnosticAllCars() {
        val handlerThread = HandlerThread("diagnosticThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            val cars = carDao.getAll().stream()
                .map { entity -> carFrom(entity) }
                .map { car -> makeDiagnosticAndSave(car) }
                .collect(Collectors.toList())

            mainHandler.sendMessage(handler.obtainMessage(DIAGNOSTIC_IS_READY, cars))
            handlerThread.quit()
        }
    }

    override fun doDiagnosticForCar(car: Car) {
        val handlerThread = HandlerThread("diagnosticThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            val carResult = makeDiagnosticAndSave(car)

            mainHandler.sendMessage(
                handler.obtainMessage(
                    DIAGNOSTIC_CAR_IS_READY,
                    makeDiagnosticAndSave(carResult)
                )
            )
            handlerThread.quit()
        }
    }

    override fun makeDiagnosticForNotification() {
        val handlerThread = HandlerThread("diagnosticThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            val cars = carDao.getAll().stream()
                .map { entity -> carFrom(entity) }
                .map { car -> makeDiagnosticAndSave(car) }
                .filter { !it.condition.contains(Condition.OK) }
                .collect(Collectors.toList())

            val line = "${cars.size} has problem, let's see it!"

            mainHandler.sendMessage(handler.obtainMessage(RESULT_DIAGNOSTIC_FOR_NOTIFICATION, line))
            handlerThread.quit()
        }
    }

    override fun createCommonPartsFor(car: Car) {
        val handlerThread = HandlerThread("createPartsThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
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

            val part = partDao.getAllForCar(car.id).stream()
                .map { entity -> partFrom(entity) }
                .collect(Collectors.toList())

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_PARTS_FOR_CAR, part))
            handlerThread.quit()
        }
    }

    override fun makeDiagnosticAndSave(car: Car): Car {
        carDao.update(carEntityFrom(car))

        car.parts = partDao.getAllForCar(car.id).stream()
            .map { entity -> partFrom(entity) }
            .collect(Collectors.toList())

        car.parts.forEach { part ->
            part.checkCondition(preferences)
            partDao.update(partEntityFrom(part))
        }

        car.checkConditions(preferences)
        carDao.update(carEntityFrom(car))

        return car
    }

    
// for delete(only for test show)
    fun createTestEntityForApp() {

        val handlerThread = HandlerThread("createPartsThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {

            var car = Car()
            car.brand = "Kia"
            car.model = "Rio"
            car.number = "1452 HA-4"
            car.vin = "56718394GHDJY567"
            car.year = 2013
            car.mileage = 43000
            car.whenMileageRefreshed = LocalDate.of(2020, 5, 24)
            car = getPartsForTestCar(car)
            car.mileage = 56500
            carDao.update(carEntityFrom(car))

            var car1 = Car()
            car1.brand = "Kia"
            car1.model = "Soul"
            car1.number = "1342 TA-6"
            car1.vin = "5671WER34HDJY567"
            car1.year = 2016
            car1.mileage = 23000
            car1.whenMileageRefreshed = LocalDate.of(2020, 4, 14)
            car1 = getPartsForTestCar(car1)
            car1.mileage = 27600
            carDao.update(carEntityFrom(car1))

            var car2 = Car()
            car2.brand = "Chevrolet"
            car2.model = "Aveo"
            car2.number = "6723 AA-2"
            car2.vin = "56718394GHDJY567"
            car2.year = 2011
            car2.mileage = 113000
            car2.whenMileageRefreshed = LocalDate.of(2020, 5, 10)
            car2 = getPartsForTestCar(car2)
            car2.mileage = 130500
            carDao.update(carEntityFrom(car2))

            var car3 = Car()
            car3.brand = "Chevrolet"
            car3.model = "Cruze"
            car3.number = "1455 HA-4"
            car3.vin = "56718394GHDJY567"
            car3.year = 2017
            car3.mileage = 23000
            car3.whenMileageRefreshed = LocalDate.of(2020, 4, 24)
            car3 = getPartsForTestCar(car3)
            car3.mileage = 37500
            carDao.update(carEntityFrom(car3))

            var car4 = Car()
            car4.brand = "Ford"
            car4.model = "Focus"
            car4.number = "6642 CC-7"
            car4.vin = "56718394GHDJY567"
            car4.year = 2011
            car4.mileage = 143000
            car4.whenMileageRefreshed = LocalDate.of(2020, 5, 30)
            car4 = getPartsForTestCar(car4)
            car4.mileage = 152500
            carDao.update(carEntityFrom(car4))

            var car5 = Car()
            car5.brand = "Opel"
            car5.model = "Corsa"
            car5.number = "1388 EA-3"
            car5.vin = "56718394GHDJY567"
            car5.year = 2010
            car5.mileage = 241000
            car5.whenMileageRefreshed = LocalDate.of(2020, 6, 16)
            car5 = getPartsForTestCar(car5)
            car5.mileage = 254500
            carDao.update(carEntityFrom(car5))

            var car6 = Car()
            car6.brand = "Gelly"
            car6.model = "FY11"
            car6.number = "7777 AA-7"
            car6.vin = "56718394GHDJY567"
            car6.year = 2020
            car6.mileage = 3000
            car6.whenMileageRefreshed = LocalDate.of(2020, 5, 18)
            car6 = getPartsForTestCar(car6)
            car6.mileage = 8001
            carDao.update(carEntityFrom(car6))

            doDiagnosticAllCars()
            handlerThread.quit()
        }
    }

    private fun getPartsForTestCar(car: Car): Car {

        car.id = carDao.insert(carEntityFrom(car))

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

        return car
    }

}