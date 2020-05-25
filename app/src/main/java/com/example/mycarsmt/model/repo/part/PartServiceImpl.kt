package com.example.mycarsmt.model.repo.part

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.Part
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.database.AppDatabase
import com.example.mycarsmt.model.database.note.NoteDao
import com.example.mycarsmt.model.database.part.PartDao
import com.example.mycarsmt.model.database.repair.RepairDao
import com.example.mycarsmt.model.repo.mappers.EntityConverter
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.partEntityFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.partFrom
import java.util.stream.Collectors

@SuppressLint("NewApi")
class PartServiceImpl(val context: Context, handler: Handler) : PartService {

    companion object {
        const val RESULT_PARTS_READED = 201
        const val RESULT_PART_READED = 202
        const val RESULT_PART_CREATED = 203
        const val RESULT_PART_UPDATED = 204
        const val RESULT_PART_DELETED = 205
        const val RESULT_PARTS_FOR_CAR = 211
        const val RESULT_NOTES_FOR_PART = 212
        const val RESULT_REPAIRS_FOR_PART = 213

    }

    private val TAG = "testmt"

    private var mainHandler: Handler
    private var partDao: PartDao
    private var noteDao: NoteDao
    private var repairDao: RepairDao

    init {
        val db: AppDatabase = AppDatabase.getInstance(context)!!
        mainHandler = handler
        partDao = db.partDao()
        noteDao = db.noteDao()
        repairDao = db.repairDao()
    }

    override fun create(part: Part) {
        var partId: Long
        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            partId = partDao.insert(partEntityFrom(part))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_PART_CREATED, partId))
            handlerThread.quit()
        }
    }

    override fun update(part: Part) {
        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            partDao.update(partEntityFrom(part))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_PART_UPDATED))
            handlerThread.quit()
        }
    }

    override fun delete(part: Part) {
        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            partDao.update(partEntityFrom(part))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_PART_DELETED))
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

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_PARTS_READED, parts))
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

    override fun readById(id: Long) {
        var part: Part
        val handlerThread = HandlerThread("readThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            part = partFrom(partDao.getById(id))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_PART_READED, part))
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

    // settings put tp init block
    var warnKMToBuy = 2000
    var warnKMToServ = 1000
    var warnDayToBuy = 30
    var warnDayToServ = 10

    //    override fun create() {
//        partDao?.insert(partEntity)
//    }
//
//    override fun read() {
//        partDao?.getById(partEntity.id)
//    }
//
//    override fun update() {
//        partDao?.update(partEntity)
//    }
//
//    override fun delete() {
//        partDao?.delete(partEntity)
//    }
//
//    override fun getMileageToRepair(): Int {
//        return partEntity.limitKM - getUsedMileage()
//    }
//
//    override fun getUsedMileage(): Int {
////        return part.mileage - part.mileageLastChange
//        return 0
//    }
//
//    override fun getDaysToRepair(): Int {
//        return partEntity.limitDays - ChronoUnit.DAYS.between(partEntity.dateLastChange, LocalDate.now()).toInt()
//    }
//
//    override fun getInfoToChange(): String {
//        if (partEntity.limitDays == 0) return "${getMileageToRepair()} km"
//        if (partEntity.limitKM == 0)   return "${getDaysToRepair()} days"
//        return "${getMileageToRepair()} km/${getDaysToRepair()} days"
//    }
//
//    override fun getRepairs(): List<RepairEntity> {
//        val repairs = repairDao?.getAllForPart(partEntity.id)
//        return if (repairs?.size!! > 0) repairs
//        else emptyList()
//    }
//
//    override fun isNeedToBuy(): Boolean {
//        if (partEntity.type == PartControlType.INSPECTION) return false
//        if (getMileageToRepairForDayOrKmIsNull() < warnKMToBuy ||
//            getDaysToRepairForDayOrKmIsNull() < warnDayToBuy) return true
//        return false
//    }
//
//    override fun isNeedToService(): Boolean {
//        if (partEntity.type == PartControlType.INSPECTION) return isOverRide()
//        if (getDaysToRepairForDayOrKmIsNull() < warnDayToServ ||
//            getMileageToRepairForDayOrKmIsNull() < warnKMToServ) return true
//        return false
//    }
//
//    override fun isNeedToInspection(): Boolean {
//        if (isOverRide()) return true
//        return false
//    }
//
//    override fun isOverRide(): Boolean {
//        if (getDaysToRepairForDayOrKmIsNull() < 0 || getMileageToRepairForDayOrKmIsNull() < 0) return true
//        return false
//    }
//
//    override fun makeService() {
////        part.mileageLastChange = part.mileage
//
////        if (part.codes == SpecialWords.INSURANCE.value) part.dateLastChange = part.dateLastChange.plusYears(1)
////        else  part.dateLastChange = LocalDate.now()
//
//        update()
//
////        val repair = Repair()
////        repair.type = "change"
////        repair.mileage = part.mileage
////        repair.carId = part.carId
////        repair.partId = part.id
////        repair.description = "auto change in context planning technical works: ${part.codes}"
////        if (part.codes == SpecialWords.INSURANCE.value)
////            repair.description = "auto increase insurance to: ${part.dateLastChange.plusYears(1)}"
////        RepairServiceImpl(context, repair).create()
//    }
//
//    override fun getLineForBuyList(): String {
//        return if(isNeedToBuy()){
//            "   -> ${partEntity.name}: ${partEntity.codes}"
//        }else{
//            ""
//        }
//    }
//
//    override fun getLineForService(): String {
//        if(partEntity.type == PartControlType.INSPECTION){
//            if (isOverRide())
//                return "    -> Make inspection for ${partEntity.name}"
//        }else{
//            if (isOverRide())
//                return "    -> !!!ATTENTION!!! ${partEntity.name}  OVERUSED ${checkDayOrKm(0, 0)}"
//            if (isNeedToService())
//                return "    -> Make service for ${partEntity.name}, do service in ${checkDayOrKm(warnDayToServ, warnKMToServ)}"
//            if (isNeedToBuy() && partEntity.limitKM == 0)
//                return "    -> Ready to continue ${partEntity.name}, left ${getDaysToRepair()} day(s)"
//            if (isNeedToBuy())
//                return "    -> Buy ${partEntity.name}, left ${checkDayOrKm(warnDayToBuy, warnKMToBuy)}"
//        }
//        return ""
//    }
//
//    private fun checkDayOrKm(warnDays: Int, warnKm: Int): String{
//        if (getDaysToRepairForDayOrKmIsNull() < warnDays && getMileageToRepairForDayOrKmIsNull() < warnKm)
//            return "${getDaysToRepair()} day(s)/${getMileageToRepair()} km"
//        if (getDaysToRepairForDayOrKmIsNull() < warnDays)
//            return "${getDaysToRepair()} day(s)"
//        if (getMileageToRepairForDayOrKmIsNull() < warnKm)
//            return "${getMileageToRepair()} km"
//        return "error"
//    }
//
//    private fun getDaysToRepairForDayOrKmIsNull(): Int {
//        return if (partEntity.limitDays == 0) 100
//        else partEntity.limitDays - ChronoUnit.DAYS.between(partEntity.dateLastChange, LocalDate.now()).toInt()
//    }
//
//    private fun getMileageToRepairForDayOrKmIsNull(): Int {
//        return if (partEntity.limitKM == 0) 10000
//        else partEntity.limitKM - getUsedMileage()
//    }
}