package com.example.mycarsmt.domain.service.car

import com.example.mycarsmt.domain.*
import io.reactivex.Flowable
import io.reactivex.Single


interface CarService {

    fun create(car: Car): Single<Car>
    fun update(car: Car): Single<Car>
    fun delete(car: Car): Single<Int>
    fun readAll(): Flowable<List<Car>>
    fun readById(id: Long): Flowable<Car>

    fun getPartsFor(car: Car): Flowable<List<Part>>
    fun getNotesFor(car: Car): Flowable<List<Note>>
    fun getRepairsFor(car: Car): Flowable<List<Repair>>

    fun getToBuyList(): Flowable<List<DiagnosticElement>>
    fun getToDoList(): Flowable<List<DiagnosticElement>>

    fun doDiagnosticAllCars()
    fun doDiagnosticForCar(car: Car)
    fun makeDiagnosticAndSave(car: Car): Car
    fun makeDiagnosticForNotification()

    fun createCommonPartsFor(car: Car)
}