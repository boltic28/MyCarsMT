package com.example.mycarsmt.model.database.car

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CarDao {

    @Insert
    fun insert(car: CarEntity): Long

    @Update
    fun update(car: CarEntity): Int

    @Delete
    fun delete(car: CarEntity): Int

    @Query("SELECT * FROM car WHERE id = :id")
    fun getById(id: Long): CarEntity

    @Query("SELECT * FROM car ORDER BY brand AND model AND number ASC")
    fun getAll(): List<CarEntity>
}
