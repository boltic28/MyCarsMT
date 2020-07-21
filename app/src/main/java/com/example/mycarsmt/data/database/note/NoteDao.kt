package com.example.mycarsmt.data.database.note

import androidx.room.*

@Dao
interface NoteDao {

    @Insert
    fun insert(noteEntity: NoteEntity): Long

    @Update
    fun update(noteEntity: NoteEntity): Int

    @Delete
    fun delete(noteEntity: NoteEntity): Int

    @Query("SELECT * FROM note")
    fun getAll(): List<NoteEntity>

    @Query("SELECT * FROM note WHERE car_id = :carId")
    fun getAllForCar(carId: Long): List<NoteEntity>

    @Query("SELECT * FROM note WHERE part_id = :partId")
    fun getAllForPart(partId: Long): List<NoteEntity>

    @Query("SELECT * FROM note WHERE id = :id")
    fun getById(id: Long): NoteEntity




//    @Query("SELECT * FROM note WHERE id = :id")
//    fun getByIdLive(id: Long): LiveData<NoteEntity>
//
//    @Query("SELECT * FROM note")
//    fun getAllLive(): LiveData<List<NoteEntity>>
//
//    @Query("SELECT note.*, car.mileage FROM note, car WHERE car.id == note.car_id AND note.id = :id")
//    fun getByIdWithMileageLive(id: Long): LiveData<NoteWithMileage>
//
//    @Query("SELECT note.*, car.mileage FROM note, car WHERE car.id == note.car_id")
//    fun getAllWithMileageLive(): LiveData<List<NoteWithMileage>>
//
//    @Query("SELECT * FROM note WHERE car_id = :carId")
//    fun getAllForCarLive(carId: Long): LiveData<List<NoteEntity>>
//
//    @Query("SELECT note.*, car.mileage FROM note, car WHERE car.id == note.car_id AND car_id = :carId")
//    fun getAllForCarWithMileageLive(carId: Long): LiveData<List<NoteWithMileage>>
//
//    @Query("SELECT * FROM note WHERE car_id = :partId")
//    fun getAllForPartLive(partId: Long): LiveData<List<NoteEntity>>



// for delete



}
