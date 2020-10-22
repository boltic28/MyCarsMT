package com.example.mycarsmt.presentation.fragments

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.datalayer.data.car.CarRepositoryImpl
import javax.inject.Inject

class SettingModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var carService: CarRepositoryImpl
    @Inject
    lateinit var preferences: SharedPreferences

    init {
        App.component.injectModel(this)
    }
}