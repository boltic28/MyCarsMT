package com.example.mycarsmt.presentation.fragments

import androidx.lifecycle.ViewModel
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.datalayer.data.car.CarRepositoryImpl
import com.example.mycarsmt.datalayer.data.note.NoteRepositoryImpl
import com.example.mycarsmt.datalayer.data.part.PartRepositoryImpl
import com.example.mycarsmt.datalayer.data.repair.RepairRepositoryImpl
import javax.inject.Inject

class PartModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var carService: CarRepositoryImpl
    @Inject
    lateinit var noteService: NoteRepositoryImpl
    @Inject
    lateinit var partRepository: PartRepositoryImpl
    @Inject
    lateinit var repairService: RepairRepositoryImpl

    init {
        App.component.injectModel(this)
    }

}