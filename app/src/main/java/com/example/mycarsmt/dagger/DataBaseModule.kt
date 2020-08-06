package com.example.mycarsmt.dagger

import android.content.Context
import android.content.SharedPreferences
import com.example.mycarsmt.data.AppDatabase
import com.example.mycarsmt.presentation.fragments.SettingFragment
import dagger.Module
import dagger.Provides
import java.util.logging.Handler
import javax.inject.Singleton

@Module
class DataBaseModule(val context: Context) {

    @Singleton
    @Provides
    fun provideDataBase(): AppDatabase{
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun providePreference(): SharedPreferences{
        return context.getSharedPreferences(
            SettingFragment.APP_PREFERENCES,
            Context.MODE_PRIVATE)
    }
}