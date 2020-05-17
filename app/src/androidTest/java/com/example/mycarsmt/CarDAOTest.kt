package com.example.mycarsmt

import androidx.room.Room
import androidx.test.InstrumentationRegistry.getContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mycarsmt.model.enums.CarCondition
import com.example.mycarsmt.model.database.AppDatabase
import com.example.mycarsmt.model.database.car.CarEntity
import com.example.mycarsmt.model.database.car.CarDao
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
        val car = CarEntity()
        car.year = 2015
        car.brand = "MB"
        car.model = "e300"
        car.mileage = 25000
        car.number = "5555 AA-7"
        car.vin = "RUIWYTEG34567788"
        car.whenMileageRefreshed = LocalDate.now()
        car.condition = CarCondition.OK

        carDao?.insert(car)
        val list = carDao?.getAll()
        val car1 = list?.get(0)

        assert(list?.size == 1)
        assert(carHelper.compareCars(car, car1!!))
    }

    @Test
    fun insertCarAndReadItAsCarWithAll() {
        val car = CarEntity()
        car.year = 2015
        car.brand = "MB"
        car.model = "e300"
        car.mileage = 25000
        car.number = "5555 AA-7"
        car.vin = "RUIWYTEG34567788"
        car.whenMileageRefreshed = LocalDate.now()
        car.condition = CarCondition.OK

        val id = carDao?.insert(car)
        val car1 = carDao?.getByIdWithAllElements(id!!)

        assert(carHelper.compareCars(car, car1!!))
    }

    @After
    @Throws(java.lang.Exception::class)
    fun closeDb(): Unit {
        db!!.close()
    }
}