package com.example.mycarsmt.model.repo.repair

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.database.AppDatabase
import com.example.mycarsmt.model.database.car.CarDao
import com.example.mycarsmt.model.database.part.PartDao
import com.example.mycarsmt.model.database.repair.RepairDao
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.carFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.partFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.repairEntityFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.repairFrom
import java.util.stream.Collectors

@SuppressLint("NewApi")
class RepairServiceImpl(var context: Context, handler: Handler): RepairService {

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

    private var mainHandler: Handler
    private var repairDao: RepairDao
    private var carDao: CarDao
    private var partDao: PartDao

    init {
        val db: AppDatabase = AppDatabase.getInstance(context)!!
        mainHandler = handler
        repairDao = db.repairDao()
        carDao = db.carDao()
        partDao = db.partDao()
    }

    override fun create(repair: Repair) {
        var repairId: Long
        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            repairId = repairDao.insert(repairEntityFrom(repair))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_REPAIR_CREATED, repairId))
            handlerThread.quit()
        }
    }

    override fun update(repair: Repair) {
        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            repairDao.update(repairEntityFrom(repair))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_REPAIR_UPDATED))
            handlerThread.quit()
        }
    }

    override fun delete(repair: Repair) {
        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            repairDao.delete(repairEntityFrom(repair))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_REPAIR_DELETED))
            //get car or part
            getCarFor(repair)
            handlerThread.quit()
        }
    }

    override fun readAll() {
        var repairs: List<Repair>
        val handlerThread = HandlerThread("readThread")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        handler.post {
            repairs = repairDao.getAll().stream().map { entity -> repairFrom(entity) }
                .collect(Collectors.toList())

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_REPAIRS_READED, repairs))
            handlerThread.quit()
        }
    }

    override fun readById(id: Long) {
        var repair: Repair
        val handlerThread = HandlerThread("readThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            repair = repairFrom(repairDao.getById(id))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_REPAIR_READED, repair))
            handlerThread.quit()
        }
    }

    override fun readAllForCar(carId: Long) {
        var repairs: List<Repair>
        val handlerThread = HandlerThread("readPartForCarThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            repairs = repairDao.getAllForCar(carId).stream().map { entity -> repairFrom(entity) }
                .collect(Collectors.toList())

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_REPAIRS_FOR_CAR, repairs))
            handlerThread.quit()
        }
    }

    override fun readAllForPart(partId: Long) {
        var repairs: List<Repair>
        val handlerThread = HandlerThread("readThread")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        handler.post {
            repairs = repairDao.getAllForPart(partId).stream().map { entity -> repairFrom(entity) }
                .collect(Collectors.toList())

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_REPAIRS_FOR_PART, repairs))
            handlerThread.quit()
        }
    }

    override fun getCarFor(repair: Repair) {
        val handlerThread = HandlerThread("getCarThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            val car = carFrom(carDao.getById(repair.carId))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_REPAIR_CAR, car))
            handlerThread.quit()
        }
    }

    override fun getPartFor(repair: Repair) {
        val handlerThread = HandlerThread("getPartThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            val part = partFrom(partDao.getById(repair.partId))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_REPAIR_PART, part))
            handlerThread.quit()
        }
    }
}