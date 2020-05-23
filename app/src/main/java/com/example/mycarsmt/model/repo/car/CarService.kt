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

//    fun getParts(car: Car): List<Part>
//    fun getNotes(car: Car): List<Note>
//    fun getRepairs(car: Car): List<Repair>

//    fun getPartsListForBuying(car: Car): List<String>
//    fun getTasksListForService(car: Car): List<String>
//    fun getMileageAsLine(car: Car): String
//    fun getDataForMileageList(car: Car): List<String>
//    fun getCountOfNotes(car: Car)
}