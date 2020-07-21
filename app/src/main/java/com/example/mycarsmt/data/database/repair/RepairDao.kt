package com.example.mycarsmt.data.database.repair

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
    fun getById(id: Long): RepairEntity

    @Query("SELECT * FROM repair")
    fun getAll(): List<RepairEntity>

    @Query("SELECT * FROM repair WHERE car_id = :carId")
    fun getAllForCar(carId: Long): List<RepairEntity>

    @Query("SELECT * FROM repair WHERE part_id = :partId")
    fun getAllForPart(partId: Long): List<RepairEntity>
}
