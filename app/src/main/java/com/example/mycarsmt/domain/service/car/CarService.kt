package com.example.mycarsmt.domain.service.car

import com.example.mycarsmt.domain.*
import io.reactivex.Flowable
import io.reactivex.Single


interface CarService {

    fun create(car: Car): Single<Long>
    fun readById(id: Long): Single<Car>
    fun update(car: Car): Single<Int>
    fun delete(car: Car): Single<Int>
    fun readAll(): Flowable<List<Car>>

    fun getPartsFor(car: Car): Flowable<List<Part>>
    fun getNotesFor(car: Car): Flowable<List<Note>>
    fun getRepairsFor(car: Car): Flowable<List<Repair>>

    fun getToBuyList(): Single<List<DiagnosticElement>>
    fun getToDoList(): Single<List<DiagnosticElement>>

    fun doDiagnosticAllCars()
    fun doDiagnosticForCar(car: Car)
    fun refresh(car: Car)
    fun makeDiagnosticForNotification()

    fun createCommonPartsFor(car: Car)
}