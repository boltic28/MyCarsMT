package com.example.mycarsmt.domain.service.car

import com.example.mycarsmt.domain.Car

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

    fun doDiagnosticAllCars()
    fun doDiagnosticForCar(car: Car)
    fun makeDiagnosticAndSave(car: Car): Car
    fun makeDiagnosticForNotification()

    fun createCommonPartsFor(car: Car)
}