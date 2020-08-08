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
//-----without mileage----
    @Query("SELECT * FROM note")
    fun getAll(): Flowable<List<NoteEntity>>

    @Query("SELECT * FROM note WHERE car_id = :carId")
    fun getAllForCar(carId: Long): Flowable<List<NoteEntity>>

    @Query("SELECT * FROM note WHERE part_id = :partId")
    fun getAllForPart(partId: Long): Flowable<List<NoteEntity>>

    @Query("SELECT * FROM note WHERE id = :id")
    fun getById(id: Long): Flowable<NoteEntity>
//------------------------
//    @Query("SELECT note.*, car.mileage FROM note, car WHERE car.id == note.car_id")
//    fun getAll(): Flowable<List<NoteWithMileage>>
//
//    @Query("SELECT note.*, car.mileage FROM note, car WHERE car.id == note.car_id AND car_id = :carId")
//    fun getAllForCar(carId: Long): Flowable<List<NoteWithMileage>>
//
//    @Query("SELECT note.*, car.mileage FROM note, car WHERE car.id == note.car_id AND part_id = :partId")
//    fun getAllForPart(partId: Long): Flowable<List<NoteWithMileage>>
//
//    @Query("SELECT note.*, car.mileage FROM note, car WHERE car.id == note.car_id AND note.id = :id")
//    fun getById(id: Long): Flowable<NoteWithMileage>
}
