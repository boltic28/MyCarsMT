package com.example.mycarsmt.presentation.fragments

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.domain.service.car.CarServiceImpl
import javax.inject.Inject

class SettingModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var carService: CarServiceImpl
    @Inject
    lateinit var preferences: SharedPreferences

    init {
        App.component.injectModel(this)
    }
}