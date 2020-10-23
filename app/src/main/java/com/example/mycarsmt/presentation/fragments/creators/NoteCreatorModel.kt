package com.example.mycarsmt.presentation.fragments.creators

import androidx.lifecycle.ViewModel
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.datalayer.data.car.CarRepositoryImpl
import com.example.mycarsmt.datalayer.data.note.NoteRepositoryImpl
import com.example.mycarsmt.businesslayer.service.part.PartServiceImpl
import com.example.mycarsmt.datalayer.data.repair.RepairRepositoryImpl
import javax.inject.Inject

class NoteCreatorModel @Inject constructor(): ViewModel()  {

    @Inject
    lateinit var carService: CarRepositoryImpl
    @Inject
    lateinit var noteService: NoteRepositoryImpl
    @Inject
    lateinit var repairService: RepairRepositoryImpl
    @Inject
    lateinit var partService: PartServiceImpl

    init {
        App.component.injectModel(this)
    }
}