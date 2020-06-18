package com.example.mycarsmt.model.repo.part

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.HandlerThread
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.Part
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.database.AppDatabase
import com.example.mycarsmt.model.database.car.CarDao
import com.example.mycarsmt.model.database.note.NoteDao
import com.example.mycarsmt.model.database.part.PartDao
import com.example.mycarsmt.model.database.repair.RepairDao
import com.example.mycarsmt.model.enums.PartControlType
import com.example.mycarsmt.model.repo.mappers.EntityConverter
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.carEntityFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.carFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.partEntityFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.partFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.repairEntityFrom
import com.example.mycarsmt.view.fragments.SettingFragment
import java.util.stream.Collectors

@SuppressLint("NewApi")
class PartServiceImpl(val context: Context, handler: Handler) : PartService {

    companion object {
        const val TAG = "testmt"

        const val RESULT_PARTS_READ = 201
        const val RESULT_PART_READ = 202
        const val RESULT_PART_CREATED = 203
        const val RESULT_PART_UPDATED = 204
        const val RESULT_PART_DELETED = 205
        const val RESULT_PART_CAR = 206
        const val RESULT_PARTS_FOR_CAR = 211
        const val RESULT_NOTES_FOR_PART = 212
        const val RESULT_REPAIRS_FOR_PART = 213
    }

    private var mainHandler: Handler
    private var partDao: PartDao
    private var noteDao: NoteDao
    private var carDao: CarDao
    private var repairDao: RepairDao
    private var preferences: SharedPreferences

    init {
        val db: AppDatabase = AppDatabase.getInstance(context)!!
        mainHandler = handler
        partDao = db.partDao()
        noteDao = db.noteDao()
        carDao = db.carDao()
        repairDao = db.repairDao()
        preferences = context.getSharedPreferences(
            SettingFragment.APP_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    override fun create(part: Part) {
        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            part.id = partDao.insert(partEntityFrom(part))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_PART_CREATED, part))
            handlerThread.quit()
        }
    }

    override fun update(part: Part) {
        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            part.checkCondition(preferences)
            partDao.update(partEntityFrom(part))

            val car = carFrom(carDao.getById(part.carId))
            car.parts = partDao.getAllForCar(car.id).stream()
                .map{entity -> partFrom(entity)}
                .collect(Collectors.toList())
            car.checkConditions(preferences)
            carDao.update(carEntityFrom(car))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_PART_UPDATED, part))
            handlerThread.quit()
        }
    }

    override fun delete(part: Part) {
        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            partDao.delete(partEntityFrom(part))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_PART_DELETED, part))
            getCarFor(part)
            handlerThread.quit()
        }
    }

    override fun readAll() {
        var parts: List<Part>
        val handlerThread = HandlerThread("readThread")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        handler.post {
            parts = partDao.getAll().stream().map { entity -> partFrom(entity) }
                .collect(Collectors.toList())

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_PARTS_READ, parts))
            handlerThread.quit()
        }
    }

    override fun readAllForCar(car: Car) {
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

    override fun readById(partId: Long) {
        var part: Part
        val handlerThread = HandlerThread("readThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            part = partFrom(partDao.getById(partId))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_PART_READ, part))
            handlerThread.quit()
        }
    }

    override fun getNotesFor(part: Part) {
        var notes: List<Note>
        val handlerThread = HandlerThread("readThread")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        handler.post {
            notes = noteDao.getAllForPart(part.id).stream().map { entity ->
                EntityConverter.noteFrom(entity) }
                .collect(Collectors.toList())

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_NOTES_FOR_PART, notes))
            handlerThread.quit()
        }
    }

    override fun getRepairsFor(part: Part) {
        var repairs: List<Repair>
        val handlerThread = HandlerThread("readThread")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        handler.post {
            repairs = repairDao.getAllForPart(part.id).stream().map { entity ->
                EntityConverter.repairFrom(entity) }
                .collect(Collectors.toList())

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_REPAIRS_FOR_PART, repairs))
            handlerThread.quit()
        }
    }

    override fun getCarFor(part: Part) {
        val handlerThread = HandlerThread("getCarThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            val car = carFrom(carDao.getById(part.carId))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_PART_CAR, car))
            handlerThread.quit()
        }
    }

    override fun addRepair(repair: Repair) {
        val handlerThread = HandlerThread("repairThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            repairDao.insert(repairEntityFrom(repair))

            handlerThread.quit()
        }
    }
}