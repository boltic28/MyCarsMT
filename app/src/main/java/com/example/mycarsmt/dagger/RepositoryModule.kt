package com.example.mycarsmt.dagger

import com.example.mycarsmt.datalayer.data.AppDatabase
import com.example.mycarsmt.datalayer.data.car.CarRepository
import com.example.mycarsmt.datalayer.data.car.CarRepositoryImpl
import com.example.mycarsmt.datalayer.data.note.NoteRepository
import com.example.mycarsmt.datalayer.data.note.NoteRepositoryImpl
import com.example.mycarsmt.datalayer.data.part.PartRepository
import com.example.mycarsmt.datalayer.data.part.PartRepositoryImpl
import com.example.mycarsmt.datalayer.data.repair.RepairRepository
import com.example.mycarsmt.datalayer.data.repair.RepairRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule(private val appDatabase: AppDatabase) {

    @Singleton
    @Provides
    fun provideCarRepository(): CarRepository =
        CarRepositoryImpl(appDatabase.carDao())

    @Singleton
    @Provides
    fun providePartRepository(): PartRepository =
        PartRepositoryImpl(appDatabase.partDao())

    @Singleton
    @Provides
    fun provideNoteRepository(): NoteRepository =
        NoteRepositoryImpl(appDatabase.noteDao())

    @Singleton
    @Provides
    fun provideRepairRepository(): RepairRepository =
        RepairRepositoryImpl(appDatabase.repairDao())
}