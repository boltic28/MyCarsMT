package com.example.mycarsmt.presentation.fragments

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.domain.service.car.CarRepositoryImpl
import com.example.mycarsmt.domain.service.note.NoteServiceImpl
import com.example.mycarsmt.domain.service.part.PartServiceImpl
import com.example.mycarsmt.domain.service.repair.RepairServiceImpl
import javax.inject.Inject

class CarModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var carService: CarRepositoryImpl
    @Inject
    lateinit var noteService: NoteServiceImpl
    @Inject
    lateinit var partService: PartServiceImpl
    @Inject
    lateinit var repairService: RepairServiceImpl
    @Inject
    lateinit var preferences: SharedPreferences

    init {
        App.component.injectModel(this)
    }
}

