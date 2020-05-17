package com.example.mycarsmt.model.repo.car

import android.widget.ImageView
import androidx.lifecycle.LiveData
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.Part
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.database.car.CarEntity
import com.example.mycarsmt.model.database.note.NoteEntity
import com.example.mycarsmt.model.database.part.PartEntity
import com.example.mycarsmt.model.database.repair.RepairEntity

interface CarService {

    fun create(car: Car): Long
    fun update(car: Car): Long
    fun delete(car: Car): Long
    fun readAll(): LiveData<List<Car>>
    fun readById(id: Long): LiveData<Car>

    fun getParts(car: Car): LiveData<List<Part>>
    fun getNotes(car: Car): LiveData<List<Note>>
    fun getRepairs(car: Car): LiveData<List<Repair>>

//    fun getPartsListForBuying(car: Car): List<String>
//    fun getTasksListForService(car: Car): List<String>
//    fun getMileageAsLine(car: Car): String
//    fun getDataForMileageList(car: Car): List<String>
//    fun getCountOfNotes(car: Car): Int?
//
//    fun setPhotoFor(imageView: ImageView)
//    fun setConditionImageFor(imageView: ImageView)
//
//    fun isOverRide(car: Car): Boolean
//    fun isNeedInspectionControl(car: Car): Boolean
//    fun isHasImportantNotes(car: Car): Boolean
//    fun isNeedService(car: Car): Boolean
//    fun isNeedCorrectOdometer(car: Car): Boolean
//
//    fun toStringBrandAndModel(car: Car): String
}