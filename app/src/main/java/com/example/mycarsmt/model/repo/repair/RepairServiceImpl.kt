package com.example.mycarsmt.model.repo.repair

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.database.AppDatabase
import com.example.mycarsmt.model.database.note.NoteWithMileage
import com.example.mycarsmt.model.database.repair.RepairEntity
import com.example.mycarsmt.model.repo.repair.RepairService
import com.example.mycarsmt.model.repo.utils.EntityConverter
import java.util.concurrent.ExecutorService
import java.util.stream.Collectors

@SuppressLint("NewApi")
class RepairServiceImpl(var context: Context): RepairService {

    private var db: AppDatabase = AppDatabase.getInstance(context)
    private val executorService: ExecutorService = db.getDatabaseExecutorService()!!
    private var noteEntityWithMileageLive: LiveData<List<RepairEntity>>? = null

    private val repairDao = db.repairDao()

    override fun create(repair: Repair): Long {
        executorService.execute {
            repair.id = this.repairDao.insert(EntityConverter.repairEntityFrom(repair))
        }
        return repair.id
    }

    override fun update(repair: Repair): Long {
        executorService.execute {
            repair.id = this.repairDao.update(EntityConverter.repairEntityFrom(repair))
        }
        return repair.id
    }

    override fun delete(repair: Repair): Long {
        executorService.execute {
            repair.id = this.repairDao.delete(EntityConverter.repairEntityFrom(repair))
        }
        return repair.id
    }

    override fun readAll(): LiveData<List<Repair>> {
        if (noteEntityWithMileageLive == null) {
            noteEntityWithMileageLive = repairDao.getAllLive()
        }

        return Transformations.map(repairDao.getAllLive()) { list ->
            list.stream()
                .map { repairEntity -> EntityConverter.repairFrom(repairEntity) }
                .collect(Collectors.toList())
        }
    }

    override fun readById(id: Long): LiveData<Repair> {
        return Transformations.map(repairDao.getByIdLive(id)) { entity ->
            EntityConverter.repairFrom(entity)
        }
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