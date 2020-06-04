package com.example.mycarsmt.model.repo.car

import android.os.Handler
import androidx.lifecycle.LiveData
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.Part
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.database.car.CarEntity

interface CarService {

    fun create(car: Car)
    fun update(car: Car)
    fun delete(car: Car)
    fun readAll()
    fun readById(id: Long)

    fun getPartsFor(car: Car)
    fun getNotesFor(car: Car)
    fun getRepairsFor(car: Car)

    fun getToBuyList()
    fun getToDoList()
//    fun getMileageAsLine(car: Car): String
//    fun getDataForMileageList(car: Car): List<String>
//    fun getCountOfNotes(car: Car)
}