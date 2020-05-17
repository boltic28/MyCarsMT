package com.example.mycarsmt.view.activities

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mycarsmt.R
import com.example.mycarsmt.model.enums.CarCondition
import com.example.mycarsmt.model.database.AppDatabase
import com.example.mycarsmt.model.database.car.CarDao
import com.example.mycarsmt.model.database.car.CarEntity
import com.example.mycarsmt.model.database.note.NoteDao
import com.example.mycarsmt.model.database.part.PartDao
import com.example.mycarsmt.model.database.part.PartEntity
import com.example.mycarsmt.model.database.repair.RepairDao
import java.time.LocalDate


class MainActivity : AppCompatActivity() {

    private val TAG = "testmt"



    private var db: AppDatabase? = null
    private var carDao: CarDao? = null
    private var partDao: PartDao? = null
    private var repairDao: RepairDao? = null
    private var noteDao: NoteDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase.getInstance(this)
        Log.d(TAG, "database created: ${db.toString()}")
        carDao = db?.carDao()
        partDao = db?.partDao()
        noteDao = db?.noteDao()
        repairDao = db?.repairDao()

        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post(Runnable {
            Log.d(TAG, "executing create operation.")
            try {
                val car = CarEntity()
                car.year = 2015
                car.brand = "MB"
                car.model = "e300"
                car.mileage = 25000
                car.number = "5555 AA-7"
                car.vin = "RUIWYTEG34567788"
                car.whenMileageRefreshed = LocalDate.now()
                car.condition = CarCondition.OK

                Log.d(TAG, "DATA_BASE: Creating new car ${car.number}")

                var id1 = carDao?.insert(car)
                var id2 = carDao?.insert(car)
                var id3 = carDao?.insert(car)

                val part = PartEntity()
                part.carId = id1!!
                part.name = "part11"
                part.codes = "123JK"
                part.dateLastChange = LocalDate.now()
                part.description = "testing1"

                val part11 = PartEntity()
                part11.carId = id1
                part11.name = "part12"
                part11.codes = "123JK"
                part11.dateLastChange = LocalDate.now()
                part11.description = "testing1"



                val part2 = PartEntity()
                part2.carId = id2!!
                part2.name = "part21"
                part2.codes = "123JK"
                part2.dateLastChange = LocalDate.now()
                part2.description = "testing"

                val part32 = PartEntity()
                part32.carId = id2
                part32.name = "part22"
                part32.codes = "123JK"
                part32.dateLastChange = LocalDate.now()
                part32.description = "testing"


                partDao?.insert(part)
                partDao?.insert(part11)

                partDao?.insert(part2)
                partDao?.insert(part32)

                Log.d(TAG, "testing room size cars is: ${carDao?.getAll()?.size}")
                Log.d(TAG, "testing room car with $id1 size parts is: ${partDao?.getAllForCar(id1)?.size}")
                Log.d(TAG, "testing room car with $id2 size parts is: ${partDao?.getAllForCar(id2)?.size}")
                Log.d(TAG, "testing room size parts is: ${partDao?.getAll()?.size}")

                val carWithElem = carDao?.getByIdWithAllElements(id1)
                Log.d(TAG, "cars elements $id1 ${carWithElem?.partEntities?.size}")
                Log.d(TAG, "cars elements $id1 ${carWithElem?.noteEntities?.size}")
                Log.d(TAG, "cars elements $id1 ${carWithElem?.repairEntities?.size}")

                Log.d(TAG, "executing test operation is over.")
            } catch (e: InterruptedException) {
                e.printStackTrace()
                Log.d(TAG, "executing create operation is failed.")
            }
            handlerThread.quit()
        })





//        val carService = CarServiceImpl(this, car)
//        carService.create()
//        carService.createListOfPart()
//        val cars = carDao?.getAll()
//        val recycler = list_item
//        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//
////        recycler.adapter = CarItemAdapter(this, cars)
//        val parts = partDao?.getAllForCar(car.id)
//        val notes = noteDao?.getAllForCar(car.id)
//        val repairs = repairDao?.getAllForCar(car.id)
//
//        val buyingList = carService.getPartsListForBuying()
//        val serviceList = carService.getTasksListForService()
    }


}
