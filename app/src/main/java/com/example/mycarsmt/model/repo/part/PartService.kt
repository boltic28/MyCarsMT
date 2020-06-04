package com.example.mycarsmt.model.repo.part

import android.widget.ImageView
import androidx.lifecycle.LiveData
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.Part
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.database.repair.RepairEntity

interface PartService{

    fun create(part: Part)
    fun update(part: Part)
    fun delete(part: Part)
    fun readAll()
    fun readAllForCar(car: Car)
    fun readById(partId: Long)

    fun getNotesFor(part: Part)
    fun getRepairsFor(part: Part)

    fun getCarFor(part: Part)
    fun addRepair(repair: Repair)

//    fun getMileageToRepair(): Int
//    fun getUsedMileage(): Int
//    fun getDaysToRepair(): Int
//    fun getPhotoFor(imageView: ImageView)
//    fun getInfoToChange(): String
//    fun getLineForBuyList(): String
//    fun getRepairs(): List<RepairEntity>
//
//    fun isNeedToBuy(): Boolean
//    fun isNeedToService(): Boolean
//    fun isNeedToInspection(): Boolean
//    fun isOverRide(): Boolean
//
//    fun makeService()
//    fun getLineForService(): String
//
//    fun getConditionImageFor(imageView: ImageView)
}