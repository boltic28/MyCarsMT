package com.example.mycarsmt.data.database.note

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface NoteDao {

    @Insert
    fun insert(noteEntity: NoteEntity): Long

    @Update
    fun update(noteEntity: NoteEntity): Int

    @Delete
    fun delete(noteEntity: NoteEntity): Int

    @Query("SELECT * FROM note")
    fun getAll(): Flowable<List<NoteEntity>>

    @Query("SELECT * FROM note WHERE car_id = :carId")
    fun getAllForCar(carId: Long): Flowable<List<NoteEntity>>

    @Query("SELECT * FROM note WHERE part_id = :partId")
    fun getAllForPart(partId: Long): Flowable<List<NoteEntity>>

    @Query("SELECT * FROM note WHERE id = :id")
    fun getById(id: Long): Flowable<NoteEntity>
}
