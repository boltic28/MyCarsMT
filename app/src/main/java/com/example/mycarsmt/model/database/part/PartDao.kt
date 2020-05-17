package com.example.mycarsmt.model.database.part

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PartDao {

    @Insert
    fun insert(partEntity: PartEntity): Long

    @Update
    fun update(partEntity: PartEntity): Long

    @Delete
    fun delete(partEntity: PartEntity): Long

    @Query("SELECT * FROM part WHERE id = :id")
    fun getByIdWithMileageLive(id: Long): LiveData<PartWithMileage>

    @Query("SELECT * FROM part")
    fun getAllLive(): LiveData<List<PartEntity>>

    @Query("SELECT * FROM part")
    fun getAllWithMileageLive(): LiveData<List<PartWithMileage>>

    @Query("SELECT part.*, car.mileage FROM part, car WHERE car.id == part.car_id AND car_id = :carId")
    fun getPartsForCarWithMileage(carId: Long): List<PartWithMileageAndElements>

    @Query("SELECT * FROM part WHERE car_id = :carId")
    fun getAllForCarLive(carId: Long): LiveData<List<PartEntity>>


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