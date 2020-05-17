package com.example.mycarsmt.model.repo.part

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.Part
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.database.AppDatabase
import com.example.mycarsmt.model.database.part.PartWithMileage
import com.example.mycarsmt.model.repo.utils.EntityConverter
import com.example.mycarsmt.model.repo.utils.EntityConverter.Companion.partEntityFrom
import java.util.concurrent.ExecutorService
import java.util.stream.Collectors

@SuppressLint("NewApi")
class PartServiceImpl(val context: Context) : PartService {

    private var db: AppDatabase = AppDatabase.getInstance(context)
    private val executorService: ExecutorService = db.getDatabaseExecutorService()!!
    private var partEntityWithMilleageLive: LiveData<List<PartWithMileage>>? = null

    private val partDao = db.partDao()
    private val noteDao = db.noteDao()
    private val repairDao = db.repairDao()

    override fun create(part: Part): Long {
        executorService.execute { part.id = partDao.insert(partEntityFrom(part)) }
        return part.id
    }

    override fun update(part: Part): Long {
        executorService.execute { part.id = partDao.update(partEntityFrom(part)) }
        return part.id
    }

    override fun delete(part: Part): Long {
        executorService.execute { part.id = partDao.delete(partEntityFrom(part)) }
        return part.id
    }

    override fun readAll(): LiveData<List<Part>> {
        if (partEntityWithMilleageLive == null) {
            partEntityWithMilleageLive = partDao.getAllWithMileageLive()
        }

        return Transformations.map(partDao.getAllWithMileageLive()) { list ->
            list.stream()
                .map { partEntity -> EntityConverter.partFrom(partEntity) }
                .collect(Collectors.toList())
        }
    }

    override fun readById(id: Long): LiveData<Part> {
        return Transformations.map(partDao.getByIdWithMileageLive(id)) { entity ->
            EntityConverter.partFrom(
                entity
            )
        }
    }

    override fun getNotes(part: Part): LiveData<List<Note>> {
        return Transformations.map(noteDao.getAllForPartLive(part.id)) { list ->
            list.stream()
                .map { noteEntity -> EntityConverter.noteFrom(noteEntity) }
                .collect(Collectors.toList())
        }
    }

    override fun getRepairs(part: Part): LiveData<List<Repair>> {
        return Transformations.map(repairDao.getAllForPartLive(part.id)) { list ->
            list.stream()
                .map { repairEntity -> EntityConverter.repairFrom(repairEntity) }
                .collect(Collectors.toList())
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