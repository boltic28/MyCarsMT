package com.example.mycarsmt.dagger

import com.example.mycarsmt.datalayer.data.AppDatabase
import com.example.mycarsmt.datalayer.data.car.CarRepository
import com.example.mycarsmt.datalayer.data.car.CarRepositoryImpl
import com.example.mycarsmt.businesslayer.service.note.NoteRepository
import com.example.mycarsmt.businesslayer.service.note.NoteRepositoryImpl
import com.example.mycarsmt.businesslayer.service.part.PartServiceImpl
import com.example.mycarsmt.businesslayer.service.repair.RepairServiceImpl
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
    fun providePartService(): PartServiceImpl = PartServiceImpl()

    @Singleton
    @Provides
    fun provideNoteRepository(): NoteRepository = NoteRepositoryImpl(appDatabase.noteDao())

    @Singleton
    @Provides
    fun provideRepairService(): RepairServiceImpl = RepairServiceImpl()
}