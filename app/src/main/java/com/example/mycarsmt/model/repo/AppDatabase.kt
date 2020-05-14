package com.example.mycarsmt.model.repo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mycarsmt.model.repo.car.Car
import com.example.mycarsmt.model.repo.car.CarDao
import com.example.mycarsmt.model.repo.note.Note
import com.example.mycarsmt.model.repo.note.NoteDao
import com.example.mycarsmt.model.repo.part.Part
import com.example.mycarsmt.model.repo.part.PartDao
import com.example.mycarsmt.model.repo.repair.Repair
import com.example.mycarsmt.model.repo.repair.RepairDao

@Database(entities = [Car::class, Part::class, Note::class, Repair::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun carDao(): CarDao
    abstract fun partDao(): PartDao
    abstract fun noteDao(): NoteDao
    abstract fun repairDao(): RepairDao


    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "carDB").build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}