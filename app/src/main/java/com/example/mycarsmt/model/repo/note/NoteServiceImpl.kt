package com.example.mycarsmt.model.repo.note

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.database.AppDatabase
import com.example.mycarsmt.model.database.note.NoteWithMileage
import com.example.mycarsmt.model.repo.note.NoteService
import com.example.mycarsmt.model.repo.utils.EntityConverter
import java.util.concurrent.ExecutorService
import java.util.stream.Collectors

@SuppressLint("NewApi")
class NoteServiceImpl(val context: Context) : NoteService {

    private var db: AppDatabase = AppDatabase.getInstance(context)!!
    private val executorService: ExecutorService = db.getDatabaseExecutorService()!!
    private var noteEntityWithMileageLive: LiveData<List<NoteWithMileage>>? = null

    private val noteDao = db.noteDao()

    override fun create(note: Note): Long {
        executorService.execute {
            note.id = this.noteDao.insert(EntityConverter.noteEntityFrom(note))
        }
        return note.id
    }

    override fun update(note: Note): Int {
        var updated = 0
        executorService.execute {
            updated = this.noteDao.update(EntityConverter.noteEntityFrom(note))
        }
        return updated
    }

    override fun delete(note: Note): Int {
        var deleted = 0
        executorService.execute {
            deleted = this.noteDao.delete(EntityConverter.noteEntityFrom(note))
        }
        return deleted
    }

    override fun readById(id: Long): LiveData<Note> {
        return Transformations.map(noteDao.getByIdWithMileageLive(id)) { entity ->
            EntityConverter.noteFrom(entity)
        }
    }

    override fun readAll(): LiveData<List<Note>> {

        if (noteEntityWithMileageLive == null) {
            noteEntityWithMileageLive = noteDao.getAllWithMileageLive()
        }

        return Transformations.map(noteDao.getAllWithMileageLive()) { list ->
            list.stream()
                .map { noteEntity -> EntityConverter.noteFrom(noteEntity) }
                .collect(Collectors.toList())
        }
    }

    //    override fun create() {
//        noteDao?.insert(noteEntity)
//    }
//
//    override fun update() {
//        noteDao?.update(noteEntity)
//    }
//
//    override fun delete() {
//        noteDao?.delete(noteEntity)
//    }
//
//    override fun checkImportantLevel(imageView: ImageView) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun isHighImportant() = noteEntity.importantLevel == NoteLevel.HIGH
//
//    override fun done() {
//        val repair = Repair()
//        repair.type = "fixing"
//        repair.mileage = note.mileage
//        repair.carId = note.carId
//        repair.partId = note.partId
//        repair.description = "${note.description} has been fixed"
//
//        RepairServiceImpl(context, repair).create()
//    }
}