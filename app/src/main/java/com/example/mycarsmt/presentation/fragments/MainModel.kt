package com.example.mycarsmt.presentation.fragments

import androidx.lifecycle.ViewModel
import com.example.mycarsmt.dagger.App
import com.example.mycarsmt.datalayer.data.car.CarRepositoryImpl
import com.example.mycarsmt.datalayer.data.note.NoteRepositoryImpl
import com.example.mycarsmt.datalayer.data.part.PartRepositoryImpl
import javax.inject.Inject

class MainModel @Inject constructor(): ViewModel() {

    companion object {
        const val TAG = "test_mt"
    }

    @Inject
    lateinit var carService: CarRepositoryImpl
    @Inject
    lateinit var partRepository: PartRepositoryImpl
    @Inject
    lateinit var noteService: NoteRepositoryImpl

    init {
        App.component.injectModel(this)
    }


}