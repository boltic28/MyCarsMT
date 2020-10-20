package com.example.mycarsmt.datalayer.di

import android.content.Context
import android.content.SharedPreferences
import com.example.mycarsmt.presentation.fragments.SettingFragment
import dagger.Provides
import javax.inject.Singleton

class PreferencesModule(val context: Context) {

   @Singleton
   @Provides
    fun providePreference(): SharedPreferences =
        context.getSharedPreferences(
            SettingFragment.APP_PREFERENCES,
            Context.MODE_PRIVATE
        )
}