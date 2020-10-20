package com.example.mycarsmt.dagger

import android.app.Application
import com.example.mycarsmt.datalayer.di.DataBaseModule
import com.example.mycarsmt.datalayer.di.PreferencesModule

class App: Application() {

    companion object{
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        val dataBaseModule =
            DataBaseModule(this)
        val repositoryModule = RepositoryModule(dataBaseModule.provideDataBase())
        val preferencesModule = PreferencesModule(this)

        component = DaggerAppComponent
            .builder()
            .createDataModule(dataBaseModule)
            .createServiceModule(repositoryModule)
            .createPreferencesModule(preferencesModule)
            .buildComponent()
    }
}