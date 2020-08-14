package com.example.mycarsmt.data.database.car

import androidx.room.*
import com.example.mycarsmt.domain.Car
import io.reactivex.Flowable
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
    fun getAll(): Flowable<List<CarEntity>>
}
