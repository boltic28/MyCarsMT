package com.example.mycarsmt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mycarsmt.model.CarCondition
import com.example.mycarsmt.model.car.CarServiceImpl
import com.example.mycarsmt.model.repo.AppDatabase
import com.example.mycarsmt.model.repo.car.Car
import com.example.mycarsmt.model.repo.car.CarDao
import com.example.mycarsmt.model.repo.note.NoteDao
import com.example.mycarsmt.model.repo.part.Part
import com.example.mycarsmt.model.repo.part.PartDao
import com.example.mycarsmt.model.repo.repair.RepairDao
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private var db: AppDatabase? = null
    private var carDao: CarDao? = null
    private var partDao: PartDao? = null
    private var repairDao: RepairDao? = null
    private var noteDao: NoteDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase.getAppDataBase(this)
        carDao = db?.carDao()
        partDao = db?.partDao()
        noteDao = db?.noteDao()
        repairDao = db?.repairDao()

        val car = Car()
        car.year = 2015
        car.brand = "MB"
        car.model = "e300"
        car.mileage = 25000
        car.number = "5555 AA-7"
        car.vin = "RUIWYTEG34567788"
        car.whenMileageRefreshed = LocalDate.now()
        car.condition = CarCondition.OK

        val parts = partDao?.getAllForCar(car.id)
        val notes = noteDao?.getAllForCar(car.id)
        val repairs = repairDao?.getAllForCar(car.id)

        val carService = CarServiceImpl(this, car)
        val buyingList = carService.getPartsListForBuying()
        val seviceList = carService.getTasksListForService()


    }
}
