package com.example.mycarsmt.model.database.car

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CarDao {

    @Insert
    fun insert(car: CarEntity): Long

    @Update
    fun update(car: CarEntity): Long

    @Delete
    fun delete(car: CarEntity): Long

    @Query("SELECT * FROM car WHERE id = :id")
    fun getById(id: Long): CarEntity

    @Query("SELECT * FROM car ORDER BY brand AND model AND number ASC")
    fun getAllLive(): LiveData<List<CarEntity>>

// for delete
    @Query("SELECT * FROM car ORDER BY brand AND model AND number ASC")
    fun getAll(): List<CarEntity>

    @Query("SELECT * FROM car WHERE id = :id")
    fun getByIdLive(id: Long): LiveData<CarEntity>

    @Query("SELECT * from car WHERE id = :id")
    fun getByIdWithAllElements(id: Long): CarWithAllElements
}
