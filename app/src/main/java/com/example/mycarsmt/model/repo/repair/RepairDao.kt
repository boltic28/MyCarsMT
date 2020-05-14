package com.example.mycarsmt.model.repo.repair

import androidx.room.*

@Dao
interface RepairDao {

    @Query("SELECT * FROM repair")
    fun getAll(): List<Repair?>?

    @Query("SELECT * FROM note WHERE carId = :carId")
    fun getAllForCar(carId: Long): List<Repair?>?

    @Query("SELECT * FROM note WHERE partId = :partId")
    fun getAllForPart(partId: Long): List<Repair?>?

    @Query("SELECT * FROM repair WHERE id = :id") //==
    fun getById(id: Long): Repair?

    @Insert
    fun insert(repair: Repair?)

    @Update
    fun update(repair: Repair?)

    @Delete
    fun delete(repair: Repair?)
}
