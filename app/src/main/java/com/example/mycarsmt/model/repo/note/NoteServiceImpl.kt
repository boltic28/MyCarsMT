package com.example.mycarsmt.model.repo.note

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.database.AppDatabase
import com.example.mycarsmt.model.database.car.CarDao
import com.example.mycarsmt.model.database.note.NoteDao
import com.example.mycarsmt.model.database.part.PartDao
import com.example.mycarsmt.model.database.repair.RepairDao
import com.example.mycarsmt.model.repo.mappers.EntityConverter
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.carFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.noteEntityFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.noteFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.partFrom
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
        const val RESULT_NOTE_CAR = 307
        const val RESULT_NOTE_PART = 308
    }

    private val TAG = "testmt"

    private var mainHandler: Handler
    private var noteDao: NoteDao
    private var carDao: CarDao
    private var partDao: PartDao
    private var repairDao: RepairDao

    init {
        val db: AppDatabase = AppDatabase.getInstance(context)!!
        mainHandler = handler
        noteDao = db.noteDao()
        carDao = db.carDao()
        partDao = db.partDao()
        repairDao = db.repairDao()
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
        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            note.id = noteDao.insert(noteEntityFrom(note))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_NOTE_CREATED, note))
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

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_NOTE_UPDATED, note))
            handlerThread.quit()
        }
    }

    override fun delete(note: Note) {
        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            noteDao.delete(noteEntityFrom(note))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_NOTE_DELETED))
            handlerThread.quit()
        }
    }

    override fun getCarFor(note: Note) {
        val handlerThread = HandlerThread("getCarThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            val car = carFrom(carDao.getById(note.carId))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_NOTE_CAR, car))
            handlerThread.quit()
        }
    }

    override fun getPartFor(note: Note) {
        val handlerThread = HandlerThread("getPartThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            val part = partFrom(partDao.getById(note.partId))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_NOTE_PART, part))
            handlerThread.quit()
        }
    }

    override fun addRepair(repair: Repair) {
        val handlerThread = HandlerThread("addRepair")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            repairDao.insert(EntityConverter.repairEntityFrom(repair))

            handlerThread.quit()
        }
    }
}