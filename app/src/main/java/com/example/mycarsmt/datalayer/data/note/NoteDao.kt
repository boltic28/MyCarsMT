package com.example.mycarsmt.datalayer.data.note

import androidx.room.*
import io.reactivex.Single

@Dao
interface NoteDao {

    @Insert
    fun insert(noteEntity: NoteEntity): Single<Long>

    @Update
    fun update(noteEntity: NoteEntity): Single<Int>

    @Delete
    fun delete(noteEntity: NoteEntity): Single<Int>

    @Query("SELECT note.*, car.mileage FROM note, car WHERE car_id == note.car_id")
    fun getAll(): Single<List<NoteWithMileage>>

    @Query("SELECT note.*, car.mileage FROM note, car WHERE car_id == note.car_id AND car_id = :carId")
    fun getAllForCar(carId: Long): Single<List<NoteWithMileage>>

    @Query("SELECT note.*, car.mileage FROM note, car WHERE car_id == note.car_id AND part_id = :partId")
    fun getAllForPart(partId: Long): Single<List<NoteWithMileage>>

    @Query("SELECT note.*, car.mileage FROM note, car WHERE note.id = :id AND car_id == note.car_id")
    fun getById(id: Long): Single<NoteWithMileage>
}
