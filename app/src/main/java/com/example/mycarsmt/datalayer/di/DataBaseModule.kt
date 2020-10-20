package com.example.mycarsmt.datalayer.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.mycarsmt.datalayer.data.AppDatabase
import com.example.mycarsmt.presentation.fragments.SettingFragment
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule(val context: Context) {

    @Singleton
    @Provides
    fun provideDataBase(): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "cars_db").build()
}