package com.example.mycarsmt.model.database.part

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PartDao {

    @Insert
    fun insert(partEntity: PartEntity): Long

    @Update
    fun update(partEntity: PartEntity): Int

    @Delete
    fun delete(partEntity: PartEntity): Int

    @Query("SELECT part.*, car.mileage FROM part, car WHERE car.id == part.car_id AND part.id = :id")
    fun getByIdWithMileageLive(id: Long): LiveData<PartWithMileage>

    @Query("SELECT * FROM part")
    fun getAllLive(): LiveData<List<PartEntity>>

    @Query("SELECT part.*, car.mileage FROM part, car WHERE car.id == part.car_id")
    fun getAllWithMileageLive(): LiveData<List<PartWithMileage>>

    @Query("SELECT part.*, car.mileage FROM part, car WHERE car.id == part.car_id AND car_id = :carId")
    fun getAllForCarWithMileageLive(carId: Long): LiveData<List<PartWithMileage>>

// for delete
    @Query("SELECT * FROM part")
    fun getAll(): List<PartEntity>

    @Query("SELECT * FROM part WHERE car_id = :carId")
    fun getAllForCar(carId: Long): List<PartEntity>

    @Query("SELECT * FROM part WHERE id = :id")
    fun getById(id: Long): PartEntity

    @Query("SELECT part.*, car.mileage FROM part, car WHERE car.id == part.car_id AND part.id = :id")
    fun getPartWithMileage(id: Long): PartWithMileageAndElements
}