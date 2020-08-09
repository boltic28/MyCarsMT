package com.example.mycarsmt

import androidx.room.Room
import androidx.test.InstrumentationRegistry.getContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mycarsmt.data.enums.Condition
import com.example.mycarsmt.data.database.AppDatabase
import com.example.mycarsmt.data.database.car.CarEntity
import com.example.mycarsmt.data.database.car.CarDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate


@RunWith(AndroidJUnit4::class)
class CarDAOTest {

    private var db: AppDatabase? = null
    private var carDao: CarDao? = null
    private val carHelper = CarTestingHelper()

    @Before
    @Throws(Exception::class)
    fun createDb(): Unit {
        db = Room.inMemoryDatabaseBuilder(
            getContext(),
            AppDatabase::class.java
        )
            .build()
        carDao = db?.carDao()
    }

    @Test
    fun insertCarAndReadIt() {

    }

    @After
    @Throws(java.lang.Exception::class)
    fun closeDb(): Unit {
        db!!.close()
    }
}