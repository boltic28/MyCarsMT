package com.example.mycarsmt.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mycarsmt.data.database.car.CarDao
import com.example.mycarsmt.data.database.car.CarEntity
import com.example.mycarsmt.data.database.note.NoteDao
import com.example.mycarsmt.data.database.note.NoteEntity
import com.example.mycarsmt.data.database.part.PartDao
import com.example.mycarsmt.data.database.part.PartEntity
import com.example.mycarsmt.data.database.repair.RepairDao
import com.example.mycarsmt.data.database.repair.RepairEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(
    entities = [CarEntity::class, PartEntity::class, NoteEntity::class, RepairEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    private val CORES = Runtime.getRuntime().availableProcessors()

    companion object {

        fun getInstance(context: Context): AppDatabase {

            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "car_app_db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun carDao(): CarDao
    abstract fun partDao(): PartDao
    abstract fun noteDao(): NoteDao
    abstract fun repairDao(): RepairDao

    open fun getDatabaseExecutorService(): ExecutorService {
        return Executors.newFixedThreadPool(CORES)
    }
}