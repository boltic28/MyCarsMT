package com.example.mycarsmt.datalayer.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mycarsmt.datalayer.data.car.CarDao
import com.example.mycarsmt.datalayer.data.car.CarEntity
import com.example.mycarsmt.datalayer.data.note.NoteDao
import com.example.mycarsmt.datalayer.data.note.NoteEntity
import com.example.mycarsmt.datalayer.data.part.PartDao
import com.example.mycarsmt.datalayer.data.part.PartEntity
import com.example.mycarsmt.datalayer.data.repair.RepairDao
import com.example.mycarsmt.datalayer.data.repair.RepairEntity

@Database(
    entities = [CarEntity::class, PartEntity::class, NoteEntity::class, RepairEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun carDao(): CarDao
    abstract fun partDao(): PartDao
    abstract fun noteDao(): NoteDao
    abstract fun repairDao(): RepairDao
}