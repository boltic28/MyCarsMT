package com.example.mycarsmt

import androidx.room.Room
import androidx.test.InstrumentationRegistry.getContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mycarsmt.datalayer.data.AppDatabase
import com.example.mycarsmt.datalayer.data.car.CarDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


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