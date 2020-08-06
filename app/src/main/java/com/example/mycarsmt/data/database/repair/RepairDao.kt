package com.example.mycarsmt.data.database.repair

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface RepairDao {

    @Insert
    fun insert(repairEntity: RepairEntity): Long

    @Update
    fun update(repairEntity: RepairEntity): Int

    @Delete
    fun delete(repairEntity: RepairEntity): Int

    @Query("SELECT * FROM repair WHERE id = :id")
    fun getById(id: Long): Flowable<RepairEntity>

    @Query("SELECT * FROM repair")
    fun getAll(): Flowable<List<RepairEntity>>

    @Query("SELECT * FROM repair WHERE car_id = :carId")
    fun getAllForCar(carId: Long): Flowable<List<RepairEntity>>

    @Query("SELECT * FROM repair WHERE part_id = :partId")
    fun getAllForPart(partId: Long): Flowable<List<RepairEntity>>
}
