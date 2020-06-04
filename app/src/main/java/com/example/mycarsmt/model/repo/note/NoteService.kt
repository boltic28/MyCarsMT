package com.example.mycarsmt.model.repo.note

import androidx.lifecycle.LiveData
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.Repair

interface NoteService{

    fun create(note: Note)
    fun update(note: Note)
    fun delete(note: Note)
    fun readById(id: Long)
    fun readAll()
    fun readAllForCar(carId: Long)
    fun readAllForPart(partId: Long)

    fun getCarFor(note: Note)
    fun getPartFor(note: Note)

    fun addRepair(repair: Repair)

//    fun checkImportantLevel(imageView: ImageView)
//    fun isHighImportant(): Boolean
//    fun done()
}