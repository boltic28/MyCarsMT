package com.example.mycarsmt.model.repo.part

import android.widget.ImageView
import androidx.lifecycle.LiveData
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.Part
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.database.repair.RepairEntity

interface PartService{

    fun create(part: Part): Long
    fun update(part: Part): Long
    fun delete(part: Part): Long
    fun readAll(): LiveData<List<Part>>
    fun readById(id: Long): LiveData<Part>

    fun getNotes(part: Part): LiveData<List<Note>>
    fun getRepairs(part: Part): LiveData<List<Repair>>

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