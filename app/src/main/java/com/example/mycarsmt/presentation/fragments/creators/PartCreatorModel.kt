package com.example.mycarsmt.presentation.fragments.creators

import androidx.lifecycle.ViewModel
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.domain.service.car.CarServiceImpl
import com.example.mycarsmt.domain.service.part.PartServiceImpl
import javax.inject.Inject

class PartCreatorModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var partService: PartServiceImpl
    @Inject
    lateinit var carService: CarServiceImpl

    init {
        App.component.injectModel(this)
    }
}