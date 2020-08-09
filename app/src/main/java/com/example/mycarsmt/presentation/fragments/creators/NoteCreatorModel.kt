package com.example.mycarsmt.presentation.fragments.creators

import androidx.lifecycle.ViewModel
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.domain.service.car.CarServiceImpl
import com.example.mycarsmt.domain.service.note.NoteServiceImpl
import com.example.mycarsmt.domain.service.repair.RepairServiceImpl
import javax.inject.Inject

class NoteCreatorModel @Inject constructor(): ViewModel()  {

    @Inject
    lateinit var carService: CarServiceImpl
    @Inject
    lateinit var noteService: NoteServiceImpl
    @Inject
    lateinit var repairService: RepairServiceImpl

    init {
        App.component.injectModel(this)
    }
}