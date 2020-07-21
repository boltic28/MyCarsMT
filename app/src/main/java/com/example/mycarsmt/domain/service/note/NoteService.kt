package com.example.mycarsmt.domain.service.note

import com.example.mycarsmt.domain.Note
import com.example.mycarsmt.domain.Repair

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