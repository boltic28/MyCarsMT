package com.example.mycarsmt.datalayer.di

import com.example.mycarsmt.datalayer.data.AppDatabase
import com.example.mycarsmt.domain.service.car.CarRepository
import com.example.mycarsmt.domain.service.car.CarRepositoryImpl
import com.example.mycarsmt.domain.service.note.NoteServiceImpl
import com.example.mycarsmt.domain.service.part.PartServiceImpl
import com.example.mycarsmt.domain.service.repair.RepairServiceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule(private val appDatabase: AppDatabase) {

    @Singleton
    @Provides
    fun provideCarRepository(): CarRepository = CarRepositoryImpl(appDatabase.carDao())

    @Singleton
    @Provides
    fun providePartService(): PartServiceImpl = PartServiceImpl()

    @Singleton
    @Provides
    fun provideNoteService(): NoteServiceImpl = NoteServiceImpl()

    @Singleton
    @Provides
    fun provideRepairService(): RepairServiceImpl = RepairServiceImpl()
}