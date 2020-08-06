package com.example.mycarsmt.data.database.part

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface PartDao {

    @Insert
    fun insert(partEntity: PartEntity): Long

    @Update
    fun update(partEntity: PartEntity): Int

    @Delete
    fun delete(partEntity: PartEntity): Int

    @Query("SELECT part.*, car.mileage FROM part, car WHERE car.id == part.car_id")
    fun getAll(): Flowable<List<PartWithMileage>>

    @Query("SELECT part.*, car.mileage FROM part, car WHERE car.id == part.car_id AND car_id = :carId")
    fun getAllForCar(carId: Long): Flowable<List<PartWithMileage>>

    @Query("SELECT part.*, car.mileage FROM part, car WHERE car.id == part.car_id AND part.id = :id")
    fun getById(id: Long): Flowable<PartWithMileage>
}