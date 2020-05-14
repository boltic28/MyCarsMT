package com.example.mycarsmt.model.repo.car

import androidx.room.*

@Dao
interface CarDao {

    @Query("SELECT * FROM car")
    fun getAll(): List<Car?>?

    @Query("SELECT * FROM car WHERE id = :id") //==
    fun getById(id: Long): Car?

    @Insert
    fun insert(car: Car?)

    @Update
    fun update(car: Car?)

    @Delete
    fun delete(car: Car?)
}
