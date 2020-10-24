package com.example.mycarsmt.presentation.fragments.creators

import androidx.lifecycle.ViewModel
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.datalayer.data.car.CarRepositoryImpl
import com.example.mycarsmt.datalayer.data.part.PartRepositoryImpl
import com.example.mycarsmt.datalayer.data.repair.RepairRepositoryImpl
import javax.inject.Inject

class RepairCreatorModel @Inject constructor(): ViewModel()  {

    @Inject
    lateinit var carService: CarRepositoryImpl
    @Inject
    lateinit var repairService: RepairRepositoryImpl
    @Inject
    lateinit var partRepository: PartRepositoryImpl


    init {
        App.component.injectModel(this)
    }
}