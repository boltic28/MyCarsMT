package com.example.mycarsmt.datalayer.data.car

import androidx.room.*
import io.reactivex.Single

@Dao
interface CarDao {

    @Insert
    fun insert(car: CarEntity): Single<Long>

    @Update
    fun update(car: CarEntity): Single<Int>

    @Delete
    fun delete(car: CarEntity): Single<Int>

    @Query("SELECT * FROM car WHERE id = :id")
    fun getById(id: Long): Single<CarEntity>

    @Query("SELECT * FROM car ORDER BY brand AND model AND number ASC")
    fun getAll(): Single<List<CarEntity>>
}
