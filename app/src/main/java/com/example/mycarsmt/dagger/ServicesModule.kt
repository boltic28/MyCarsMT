package com.example.mycarsmt.dagger

import android.content.SharedPreferences
import com.example.mycarsmt.data.AppDatabase
import com.example.mycarsmt.data.database.car.CarDao
import com.example.mycarsmt.domain.service.car.CarServiceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ServicesModule(private val appDatabase: AppDatabase) {

    @Singleton
    @Provides
    fun provideCarService(): CarServiceImpl = CarServiceImpl()

    //todo other services for parts, repairs, notes

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