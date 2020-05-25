package com.example.mycarsmt.model.repo.note

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.database.AppDatabase
import com.example.mycarsmt.model.database.note.NoteDao
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.noteEntityFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.noteFrom
import java.util.stream.Collectors

@SuppressLint("NewApi")
class NoteServiceImpl(val context: Context, handler: Handler) : NoteService {

    companion object {
        const val RESULT_NOTES_READED = 301
        const val RESULT_NOTE_READED = 302
        const val RESULT_NOTE_CREATED = 303
        const val RESULT_NOTE_UPDATED = 304
        const val RESULT_NOTE_DELETED = 305
        const val RESULT_NOTE_FOR_CAR = 306
    }

    private val TAG = "testmt"

    private var mainHandler: Handler
    private var noteDao: NoteDao

    init {
        val db: AppDatabase = AppDatabase.getInstance(context)!!
        mainHandler = handler
        noteDao = db.noteDao()
    }

    override fun readAll() {
        var notes: List<Note>
        val handlerThread = HandlerThread("readThread")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        handler.post {
            notes = noteDao.getAll().stream().map { entity -> noteFrom(entity) }
                .collect(Collectors.toList())

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_NOTES_READED, notes))
            handlerThread.quit()
        }
    }

    override fun readById(id: Long) {
        var note: Note
        val handlerThread = HandlerThread("readThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            note = noteFrom(noteDao.getById(id))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_NOTE_READED, note))
            handlerThread.quit()
        }
    }

    override fun readAllForCar(carId: Long) {
        var notes: List<Note>
        val handlerThread = HandlerThread("readThread")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        handler.post {
            notes = noteDao.getAllForCar(carId).stream().map { entity -> noteFrom(entity) }
                .collect(Collectors.toList())

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_NOTE_FOR_CAR, notes))
            handlerThread.quit()
        }
    }

    override fun readAllForPart(partId: Long) {
        var notes: List<Note>
        val handlerThread = HandlerThread("readThread")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        handler.post {
            notes = noteDao.getAllForPart(partId).stream().map { entity -> noteFrom(entity) }
                .collect(Collectors.toList())

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_NOTE_FOR_CAR, notes))
            handlerThread.quit()
        }
    }

    override fun create(note: Note) {
        var carId: Long
        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            carId = noteDao.insert(noteEntityFrom(note))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_NOTE_CREATED, carId))
            handlerThread.quit()
        }
    }

    override fun update(note: Note) {

        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            noteDao.update(noteEntityFrom(note))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_NOTE_UPDATED))
            handlerThread.quit()
        }
    }

    override fun delete(note: Note) {
        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            noteDao.update(noteEntityFrom(note))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_NOTE_DELETED))
            handlerThread.quit()
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