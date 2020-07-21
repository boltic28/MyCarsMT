package com.example.mycarsmt.domain.service.part

import com.example.mycarsmt.domain.Car
import com.example.mycarsmt.domain.Part
import com.example.mycarsmt.domain.Repair

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
}