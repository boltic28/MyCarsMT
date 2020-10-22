package com.example.mycarsmt.presentation.fragments.creators

import androidx.lifecycle.ViewModel
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.datalayer.data.car.CarRepositoryImpl
import javax.inject.Inject

class CarCreatorModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var carService: CarRepositoryImpl

    init {
        App.component.injectModel(this)
    }
}