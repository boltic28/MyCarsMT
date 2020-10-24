package com.example.mycarsmt.presentation.fragments.creators

import androidx.lifecycle.ViewModel
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.datalayer.data.car.CarRepositoryImpl
import com.example.mycarsmt.datalayer.data.part.PartRepositoryImpl
import javax.inject.Inject

class PartCreatorModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var partRepository: PartRepositoryImpl
    @Inject
    lateinit var carService: CarRepositoryImpl

    init {
        App.component.injectModel(this)
    }
}