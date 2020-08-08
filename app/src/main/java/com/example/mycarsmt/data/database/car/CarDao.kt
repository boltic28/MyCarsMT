package com.example.mycarsmt.data.database.car

import androidx.room.*
import com.example.mycarsmt.domain.Car
import io.reactivex.Flowable

@Dao
interface CarDao {

    @Insert
    fun insert(car: CarEntity): Long

    @Update
    fun update(car: CarEntity): Int

    @Delete
    fun delete(car: CarEntity): Int

    @Query("SELECT * FROM car WHERE id = :id")
    fun getById(id: Long):Flowable<CarEntity>

    @Query("SELECT * FROM car ORDER BY brand AND model AND number ASC")
    fun getAll(): Flowable<List<CarEntity>>
}
