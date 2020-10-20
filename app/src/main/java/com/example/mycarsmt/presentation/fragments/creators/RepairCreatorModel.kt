package com.example.mycarsmt.presentation.fragments.creators

import androidx.lifecycle.ViewModel
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.domain.service.car.CarRepositoryImpl
import com.example.mycarsmt.domain.service.part.PartServiceImpl
import com.example.mycarsmt.domain.service.repair.RepairServiceImpl
import javax.inject.Inject

class RepairCreatorModel @Inject constructor(): ViewModel()  {

    @Inject
    lateinit var carService: CarRepositoryImpl
    @Inject
    lateinit var repairService: RepairServiceImpl
    @Inject
    lateinit var partService: PartServiceImpl


    init {
        App.component.injectModel(this)
    }
}