package com.example.mycarsmt.model.repo.note

import androidx.room.*

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getAll(): List<Note?>?

    @Query("SELECT * FROM note WHERE carId = :carId")
    fun getAllForCar(carId: Long): List<Note?>?

    @Query("SELECT * FROM note WHERE carId = :partId")
    fun getAllForPart(partId: Long): List<Note?>?

    @Query("SELECT * FROM note WHERE id = :id") //==
    fun getById(id: Long): Note?

    @Insert
    fun insert(note: Note?)

    @Update
    fun update(note: Note?)

    @Delete
    fun delete(note: Note?)
}
