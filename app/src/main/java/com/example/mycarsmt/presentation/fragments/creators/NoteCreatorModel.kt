package com.example.mycarsmt.presentation.fragments.creators

import androidx.lifecycle.ViewModel
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.datalayer.data.car.CarRepositoryImpl
import com.example.mycarsmt.businesslayer.service.note.NoteRepositoryImpl
import com.example.mycarsmt.businesslayer.service.part.PartServiceImpl
import com.example.mycarsmt.businesslayer.service.repair.RepairServiceImpl
import javax.inject.Inject

class NoteCreatorModel @Inject constructor(): ViewModel()  {

    @Inject
    lateinit var carService: CarRepositoryImpl
    @Inject
    lateinit var noteService: NoteRepositoryImpl
    @Inject
    lateinit var repairService: RepairServiceImpl
    @Inject
    lateinit var partService: PartServiceImpl

    init {
        App.component.injectModel(this)
    }
}