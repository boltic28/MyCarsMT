package com.example.mycarsmt.dagger

import com.example.mycarsmt.data.database.AppDatabase
import com.example.mycarsmt.domain.service.car.CarServiceImpl
import com.example.mycarsmt.domain.service.note.NoteServiceImpl
import com.example.mycarsmt.domain.service.part.PartServiceImpl
import com.example.mycarsmt.domain.service.repair.RepairServiceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ServicesModule(private val appDatabase: AppDatabase) {

    @Singleton
    @Provides
    fun provideCarService(): CarServiceImpl = CarServiceImpl()

    @Singleton
    @Provides
    fun providePartService(): PartServiceImpl = PartServiceImpl()

    @Singleton
    @Provides
    fun provideNoteService(): NoteServiceImpl = NoteServiceImpl()

    @Singleton
    @Provides
    fun provideRepairService(): RepairServiceImpl = RepairServiceImpl()

    @Singleton
    @Provides
    fun provideCarDao() = appDatabase.carDao()

    @Singleton
    @Provides
    fun providePartDao() = appDatabase.partDao()

    @Singleton
    @Provides
    fun provideNoteDao() = appDatabase.noteDao()

    @Singleton
    @Provides
    fun provideRepairDao() = appDatabase.repairDao()

    @Singleton
    @Provides
    fun provideExecutor() = appDatabase.getDatabaseExecutorService()
}