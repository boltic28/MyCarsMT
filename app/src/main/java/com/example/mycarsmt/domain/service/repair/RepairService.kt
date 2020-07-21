package com.example.mycarsmt.domain.service.repair

import com.example.mycarsmt.domain.Repair

interface RepairService{

    fun create(repair: Repair)
    fun update(repair: Repair)
    fun delete(repair: Repair)
    fun readAll()
    fun readById(id: Long)
    fun readAllForCar(carId: Long)
    fun readAllForPart(partId: Long)

    fun getCarFor(repair: Repair)
    fun getPartFor(repair: Repair)
}