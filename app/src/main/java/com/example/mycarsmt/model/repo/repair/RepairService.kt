package com.example.mycarsmt.model.repo.repair

import androidx.lifecycle.LiveData
import com.example.mycarsmt.model.Repair

interface RepairService{

    fun create(repair: Repair): Long
    fun update(repair: Repair)
    fun delete(repair: Repair)
    fun readAll()
    fun readAllForCar(carId: Long)
    fun readAllForPart(partId: Long)
    fun readById(id: Long)


//    fun toStringForCarHistory(): String
}