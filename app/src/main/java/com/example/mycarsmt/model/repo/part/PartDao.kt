package com.example.mycarsmt.model.repo.part

import androidx.room.*

@Dao
interface PartDao {

    @Query("SELECT * FROM part")
    fun getAll(): List<Part?>?

    @Query("SELECT * FROM part WHERE carId = :carId")
    fun getAllForCar(carId: Long): List<Part?>?

    @Query("SELECT * FROM part WHERE id = :id")
    fun getById(id: Long): Part?

    @Insert
    fun insert(part: Part?)

    @Update
    fun update(part: Part?)

    @Delete
    fun delete(part: Part?)
}