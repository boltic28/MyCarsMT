package com.example.mycarsmt.data.database.repair

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface RepairDao {

    @Insert
    fun insert(repairEntity: RepairEntity): Single<Long>

    @Update
    fun update(repairEntity: RepairEntity): Single<Int>

    @Delete
    fun delete(repairEntity: RepairEntity): Single<Int>

    @Query("SELECT * FROM repair WHERE id = :id")
    fun getById(id: Long): Single<RepairEntity>

    @Query("SELECT * FROM repair ORDER BY mileage")
    fun getAll(): Single<List<RepairEntity>>

    @Query("SELECT * FROM repair WHERE car_id = :carId ORDER BY mileage")
    fun getAllForCar(carId: Long): Single<List<RepairEntity>>

    @Query("SELECT * FROM repair WHERE part_id = :partId ORDER BY mileage")
    fun getAllForPart(partId: Long): Single<List<RepairEntity>>

    @Query("SELECT * FROM repair WHERE car_id = :carId ORDER BY mileage")
    fun getAllForCarList(carId: Long): List<RepairEntity>

    @Query("SELECT * FROM repair WHERE part_id = :partId ORDER BY mileage")
    fun getAllForPartList(partId: Long): List<RepairEntity>
}
