package com.example.mycarsmt.domain.service.car

import com.example.mycarsmt.domain.*
import io.reactivex.Flowable
import io.reactivex.Single


interface CarService {

    fun create(car: Car): Single<Long>
    fun getById(id: Long): Single<Car>
    fun update(car: Car): Single<Int>
    fun delete(car: Car): Single<Int>
    fun getAll(): Single<List<Car>>

//    fun getPartsFor(car: Car): Single<List<Part>>
//    fun getNotesFor(car: Car): Single<List<Note>>
//    fun getRepairsFor(car: Car): Single<List<Repair>>

    fun getToBuyList(): Single<List<DiagnosticElement>>
    fun getToDoList(): Single<List<DiagnosticElement>>

    fun refresh(car: Car)
    fun refreshAll()

//    fun doDiagnosticForCar(car: Car)
    fun makeDiagnosticForNotification()

    fun createCommonPartsFor(car: Car)
}