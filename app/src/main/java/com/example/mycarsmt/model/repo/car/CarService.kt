package com.example.mycarsmt.model.repo.car

import androidx.lifecycle.LiveData
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.Part
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.database.car.CarEntity

interface CarService {

    fun create(car: Car): Long
    fun update(car: Car): Int
    fun delete(car: Car): Int
    fun readAll(): LiveData<List<Car>>
    fun readById(id: Long): LiveData<Car>

    fun getParts(car: Car): LiveData<List<Part>>
    fun getNotes(car: Car): LiveData<List<Note>>
    fun getRepairs(car: Car): LiveData<List<Repair>>

//    fun getPartsListForBuying(car: Car): List<String>
//    fun getTasksListForService(car: Car): List<String>
//    fun getMileageAsLine(car: Car): String
//    fun getDataForMileageList(car: Car): List<String>
    fun getCountOfNotes(car: Car): Int?


    fun readAllE():LiveData<List<CarEntity>>
}