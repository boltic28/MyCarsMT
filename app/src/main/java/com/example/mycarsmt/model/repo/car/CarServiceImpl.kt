package com.example.mycarsmt.model.repo.car

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import com.example.mycarsmt.model.*
import com.example.mycarsmt.model.database.AppDatabase
import com.example.mycarsmt.model.database.car.CarDao
import com.example.mycarsmt.model.database.note.NoteDao
import com.example.mycarsmt.model.database.part.PartDao
import com.example.mycarsmt.model.database.repair.RepairDao
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.carEntityFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.carFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.noteFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.partFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.repairFrom
import java.util.stream.Collectors

class CarServiceImpl(context: Context, handler: Handler) : CarService {

    companion object {
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

    }

    private val TAG = "testmt"

    private var mainHandler: Handler
    private var carDao: CarDao
    private var partDao: PartDao
    private var noteDao: NoteDao
    private var repairDao: RepairDao

    init {
        val db: AppDatabase = AppDatabase.getInstance(context)!!
        mainHandler = handler
        carDao = db.carDao()
        partDao = db.partDao()
        noteDao = db.noteDao()
        repairDao = db.repairDao()
    }

    //settings
    val howMuchDaysBeetweenCorrectOdo = 15

    override fun readAll() {
        var cars: List<Car>
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

        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            carDao.update(carEntityFrom(car))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_CAR_UPDATED, car))
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

                    val element = DiagnosticElement(car, car.getListToBuy())

                    if (element.list.isNotEmpty()) {
                        buyList.add(DiagnosticElement(car, car.getListToBuy()))
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

                    val element = DiagnosticElement(car, car.getListToBuy())

                    if (element.list.isNotEmpty()) {
                        toDoList.add(DiagnosticElement(car, car.getListToDo()))
                    }
                }

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_TO_DO_LIST, toDoList))
            handlerThread.quit()
        }
    }

//    override fun getMileageAsLine(): String {
//        return "${car.brand} ${car.model} ${car.number}: ${car.mileage}km"
//    }
//
//    override fun getDataForMileageList(): List<String> {
//        val list: MutableList<String> = mutableListOf()
//        list.add("${car.brand} ${car.model} (${car.number}):")
//        list.add("\t ${car.mileage} km")
//        return list
//    }
//
}