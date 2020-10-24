package com.example.mycarsmt.datalayer.data.part

import androidx.room.*
import io.reactivex.Single

@Dao
interface PartDao {

    @Insert
    fun insert(partEntity: PartEntity): Single<Long>

    @Update
    fun update(partEntity: PartEntity): Single<Int>

    @Delete
    fun delete(partEntity: PartEntity): Single<Int>

    @Query("SELECT part.*, car.mileage FROM part, car WHERE car.id == part.car_id")
    fun getAll(): Single<List<PartWithMileage>>

    @Query("SELECT part.*, car.mileage FROM part, car WHERE car.id == part.car_id AND car_id = :carId")
    fun getAllForCar(carId: Long): Single<List<PartWithMileage>>

    @Query("SELECT part.*, car.mileage FROM part, car WHERE car.id == part.car_id AND part.id = :id")
    fun getById(id: Long): Single<PartWithMileage>
}