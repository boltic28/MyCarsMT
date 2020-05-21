package com.example.mycarsmt.model.database.repair

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RepairDao {

    @Insert
    fun insert(repairEntity: RepairEntity): Long

    @Update
    fun update(repairEntity: RepairEntity): Int

    @Delete
    fun delete(repairEntity: RepairEntity): Int

    @Query("SELECT * FROM repair WHERE id = :id")
    fun getByIdLive(id: Long): LiveData<RepairEntity>

    @Query("SELECT * FROM repair WHERE car_id = :carId")
    fun getAllForCarLive(carId: Long): LiveData<List<RepairEntity>>

    @Query("SELECT * FROM repair WHERE part_id = :partId")
    fun getAllForPartLive(partId: Long): LiveData<List<RepairEntity>>


// for delete
    @Query("SELECT * FROM repair")
    fun getAll(): List<RepairEntity>

    @Query("SELECT * FROM repair")
    fun getAllLive(): LiveData<List<RepairEntity>>

    @Query("SELECT * FROM repair WHERE car_id = :carId")
    fun getAllForCar(carId: Long): List<RepairEntity>

    @Query("SELECT * FROM repair WHERE part_id = :partId")
    fun getAllForPart(partId: Long): List<RepairEntity>

    @Query("SELECT * FROM repair WHERE id = :id")
    fun getById(id: Long): RepairEntity


}
