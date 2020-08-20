package com.example.mycarsmt.presentation.fragments

import androidx.lifecycle.ViewModel
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.domain.service.car.CarServiceImpl
import com.example.mycarsmt.domain.service.note.NoteServiceImpl
import com.example.mycarsmt.domain.service.part.PartServiceImpl
import javax.inject.Inject

class MainModel @Inject constructor(): ViewModel() {

    companion object {
        const val TAG = "test_mt"
    }

    @Inject
    lateinit var carService: CarServiceImpl
    @Inject
    lateinit var partService: PartServiceImpl
    @Inject
    lateinit var noteService: NoteServiceImpl

    init {
        App.component.injectModel(this)

    }


}