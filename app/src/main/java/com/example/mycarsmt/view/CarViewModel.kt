package com.example.mycarsmt.view

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.repo.car.CarServiceImpl

class CarViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "testmt"
    private val service = CarServiceImpl(getApplication<Application>().applicationContext)
    private lateinit var cars: LiveData<List<Car>>

//delete
    fun getService() = service

    fun getAll(): LiveData<List<Car>>{
        cars = service.readAll()
        Log.d(TAG, "VM_CAR: list of cars: LiveData has ${cars.hasObservers()} observers")
        return cars
    }

    fun create(car: Car): Long{
        Log.d(TAG, "VM_CAR: create car: ${car.brand} ")
        return service.create(car)
    }

    fun remove(car: Car): Int{
        Log.d(TAG, "VM_CAR: delete car: ${car.brand} ")
        return service.delete(car)
    }
}