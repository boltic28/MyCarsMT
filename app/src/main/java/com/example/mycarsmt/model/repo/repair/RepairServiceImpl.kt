package com.example.mycarsmt.model.repo.repair

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.database.AppDatabase
import com.example.mycarsmt.model.database.repair.RepairDao

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
    }

    private val TAG = "testmt"

    private var mainHandler: Handler
    private var repairDao: RepairDao

    init {
        val db: AppDatabase = AppDatabase.getInstance(context)!!
        mainHandler = handler
        repairDao = db.repairDao()
    }

    override fun create(repair: Repair) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(repair: Repair) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(repair: Repair) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readById(id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readAllForCar(carId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readAllForPart(partId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //    override fun create() {
//        repairDao?.insert(repairEntity)
//    }
//
//    override fun update() {
//        repairDao?.update(repairEntity)
//    }
//
//    override fun delete() {
//        repairDao?.delete(repairEntity)
//    }
//
//    override fun toStringForCarHistory() =
//            "${repairEntity.date} ${repairEntity.mileage} km: \n" +
//                    "\t $${repairEntity.cost}, ${repairEntity.type} - ${repairEntity.description} \n"
}