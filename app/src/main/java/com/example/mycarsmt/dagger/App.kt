package com.example.mycarsmt.dagger

import android.app.Application

class App: Application() {

    companion object{
        val TAG = "myCars"
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        val dataBaseModule = DataBaseModule(this)
        val servicesModule = ServicesModule(dataBaseModule.provideDataBase())

        component = DaggerAppComponent
            .builder()
            .createDataModule(dataBaseModule)
            .createServiceModule(servicesModule)
            .buildComponent()
    }
}