package com.example.mycarsmt.model.repo.car

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.Part
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.database.AppDatabase
import com.example.mycarsmt.model.database.car.CarDao
import com.example.mycarsmt.model.database.note.NoteDao
import com.example.mycarsmt.model.database.part.PartDao
import com.example.mycarsmt.model.database.repair.RepairDao
import com.example.mycarsmt.model.enums.Condition
import com.example.mycarsmt.model.enums.NoteLevel
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.carEntityFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.carFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.noteEntityFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.noteFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.partEntityFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.partFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.repairEntityFrom
import com.example.mycarsmt.model.repo.mappers.EntityConverter.Companion.repairFrom
import java.time.LocalDate
import java.util.stream.Collectors

class CarServiceImpl(context: Context, handler: Handler) : CarService {

    companion object {
        const val RESULT_CARS_READED = 101
        const val RESULT_CAR_READED = 102
        const val RESULT_CAR_CREATED = 103
        const val RESULT_CAR_UPDATED = 104
        const val RESULT_CAR_DELETED = 105
        const val RESULT_PARTS_FOR_CAR = 111
        const val RESULT_NOTES_FOR_CAR = 112
        const val RESULT_REPAIRS_FOR_CAR = 113

    }

    private val TAG = "testmt"

    private var mainHandler: Handler
    private var carDao: CarDao
    private var partDao: PartDao
    private var noteDao: NoteDao
    private var repairDao: RepairDao

    init {
        val db: AppDatabase = AppDatabase.getInstance(context)!!
        mainHandler = handler
        carDao = db.carDao()
        partDao = db.partDao()
        noteDao = db.noteDao()
        repairDao = db.repairDao()
    }

    //settings
    val howMuchDaysBeetweenCorrectOdo = 15

    override fun readAll() {
        var cars: List<Car>
        val handlerThread = HandlerThread("readThread")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        handler.post {
            cars = carDao.getAll().stream().map { carEntity -> carFrom(carEntity) }
                .collect(Collectors.toList())

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_CARS_READED, cars))
            handlerThread.quit()
        }
    }

    override fun readById(id: Long) {
        var car: Car
        val handlerThread = HandlerThread("readThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            car = carFrom(carDao.getById(id))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_CAR_READED, car))
            handlerThread.quit()
        }
    }

    override fun create(car: Car) {
        var carId: Long
        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            carId = carDao.insert(carEntityFrom(car))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_CAR_CREATED, carId))
            handlerThread.quit()
        }
    }

    override fun update(car: Car) {

        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            carDao.update(carEntityFrom(car))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_CAR_UPDATED))
            handlerThread.quit()
        }
    }

    override fun delete(car: Car) {
        val handlerThread = HandlerThread("createThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            carDao.update(carEntityFrom(car))

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_CAR_DELETED))
            handlerThread.quit()
        }
    }

    override fun getPartsFor(car: Car) {
        var parts: List<Part>
        val handlerThread = HandlerThread("readPartForCarThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            parts = partDao.getAllForCar(car.id).stream().map { entity -> partFrom(entity) }.collect(Collectors.toList())

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_PARTS_FOR_CAR, parts))
            handlerThread.quit()
        }
    }

    override fun getNotesFor(car: Car) {
        var notes: List<Note>
        val handlerThread = HandlerThread("readNoteForCarThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            notes = noteDao.getAllForCar(car.id).stream()
                .map { entity -> noteFrom(entity) }
                .collect(Collectors.toList())

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_NOTES_FOR_CAR, notes))
            handlerThread.quit()
        }
    }

    override fun getRepairsFor(car: Car) {
        var repairs: List<Repair>
        val handlerThread = HandlerThread("readRepairForCarThread")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)
        handler.post {
            repairs = repairDao.getAllForCar(car.id).stream()
                .map { entity -> repairFrom(entity) }
                .collect(Collectors.toList())

            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_REPAIRS_FOR_CAR, repairs))
            handlerThread.quit()
        }
    }

    //    override fun getCountOfNotes(car: Car) {
//        var size: Int
//        val handlerThread = HandlerThread("createThread")
//        handlerThread.start()
//        val looper = handlerThread.looper
//        val handler = Handler(looper)
//        handler.post {
//            size = noteDao.getAllForCar(car.id).size
//
//            mainHandler.sendMessage(mainHandler.obtainMessage(RESULT_CAR_NOTES_COUNT, size))
//            handlerThread.quit()
//        }
//    }

    //=============================

    fun testing() {
        Log.d(TAG, "TESTING: Creating new cars ....")
        val car1 = Car()
        car1.year = 2015
        car1.brand = "MB"
        car1.model = "e300"
        car1.mileage = 25000
        car1.number = "5555 AA-7"
        car1.vin = "RUIWYTEG34567788"
        car1.whenMileageRefreshed = LocalDate.now()
        car1.condition = listOf(Condition.MAKE_INSPECTION)

        val car2 = Car()
        car2.year = 2013
        car2.brand = "BMW"
        car2.model = "320"
        car2.mileage = 67000
        car2.number = "1111 AA-7"
        car2.vin = "RUIWYTEG34567788"
        car2.whenMileageRefreshed = LocalDate.now()
        car2.condition =
            listOf(Condition.MAKE_INSPECTION, Condition.MAKE_SERVICE, Condition.BUY_PARTS)

        val car3 = Car()
        car3.year = 2018
        car3.brand = "Ford"
        car3.model = "e300"
        car3.mileage = 46000
        car3.number = "1234 AA-7"
        car3.vin = "RUIWYTEG34567788"
        car3.whenMileageRefreshed = LocalDate.now()
        car3.condition = listOf(Condition.MAKE_SERVICE, Condition.BUY_PARTS)

        val car4 = Car()
        car4.year = 2020
        car4.brand = "Opel"
        car4.model = "mokka"
        car4.mileage = 12000
        car4.number = "7622 AA-7"
        car4.vin = "RUIWYTEG34567788"
        car4.whenMileageRefreshed = LocalDate.now()
        car4.condition = listOf(Condition.BUY_PARTS, Condition.ATTENTION)

        Log.d(
            TAG,
            "TESTING: executing create operation. ${car1.brand}, ${car2.brand}, ${car3.brand}, ${car4.brand}"
        )
        val id1 = carDao.insert(carEntityFrom(car1))
        val id2 = carDao.insert(carEntityFrom(car2))
        val id3 = carDao.insert(carEntityFrom(car3))
        val id4 = carDao.insert(carEntityFrom(car4))

        val part1 = Part()
        part1.carId = id1
        part1.mileage = car1.mileage
        part1.name = "part11"
        part1.codes = "123JK"
        part1.dateLastChange = LocalDate.now()
        part1.description = "testing1"
        part1.condition = listOf(Condition.BUY_PARTS)

        val part2 = Part()
        part2.carId = id1
        part2.mileage = car3.mileage
        part2.name = "part21"
        part2.codes = "123JK"
        part2.dateLastChange = LocalDate.now()
        part2.description = "testing"
        part2.condition = listOf(Condition.ATTENTION)

        val part3 = Part()
        part3.carId = id2
        part3.mileage = car1.mileage
        part3.name = "part12"
        part3.codes = "123JK"
        part3.dateLastChange = LocalDate.now()
        part3.description = "testing1"
        part2.condition = listOf(Condition.MAKE_SERVICE)

        val part4 = Part()
        part4.carId = id2
        part4.mileage = car3.mileage
        part4.name = "part22"
        part4.codes = "123JK"
        part4.dateLastChange = LocalDate.now()
        part4.description = "testing"
        part2.condition = listOf(Condition.OK)


        val partId1 = partDao.insert(partEntityFrom(part1))
        val partId2 = partDao.insert(partEntityFrom(part2))
        val partId3 = partDao.insert(partEntityFrom(part3))
        val partId4 = partDao.insert(partEntityFrom(part4))

        val note1 = Note()
        note1.carId = id1
        note1.date = LocalDate.now()
        note1.description = "some trouble with car"
        note1.importantLevel = NoteLevel.MIDDLE
        note1.partId = partId2

        val note2 = Note()
        note2.carId = id2
        note2.date = LocalDate.now()
        note2.description = "some trouble with car"
        note2.importantLevel = NoteLevel.MIDDLE
        note2.partId = partId4

        val note3 = Note()
        note3.carId = id1
        note3.date = LocalDate.now()
        note3.description = "some trouble with car"
        note3.importantLevel = NoteLevel.MIDDLE
        note3.partId = partId1

        val note4 = Note()
        note4.carId = id2
        note4.date = LocalDate.now()
        note4.description = "some trouble with car"
        note4.importantLevel = NoteLevel.MIDDLE
        note4.partId = partId3

        val noteid1 = noteDao.insert(noteEntityFrom(note1))
        val noteid2 = noteDao.insert(noteEntityFrom(note2))
        val noteid3 = noteDao.insert(noteEntityFrom(note3))
        val noteid4 = noteDao.insert(noteEntityFrom(note4))

        val repair1 = Repair()
        repair1.carId = id1
        repair1.partId = partId1
        repair1.mileage = car1.mileage
        repair1.cost = 24
        repair1.date = LocalDate.now()
        repair1.type = "change"
        repair1.description = "buy new element"

        val repair2 = Repair()
        repair2.carId = id1
        repair2.partId = partId2
        repair2.mileage = car1.mileage
        repair2.cost = 24
        repair2.date = LocalDate.now()
        repair2.type = "change"
        repair2.description = "buy new element"

        val repair3 = Repair()
        repair3.carId = id2
        repair3.partId = partId4
        repair3.mileage = car1.mileage
        repair3.cost = 24
        repair3.date = LocalDate.now()
        repair3.type = "change"
        repair3.description = "buy new element"

        val repair4 = Repair()
        repair4.carId = id2
        repair4.partId = partId4
        repair4.mileage = car1.mileage
        repair4.cost = 24
        repair4.date = LocalDate.now()
        repair4.type = "change"
        repair4.description = "buy new element"

        repairDao.insert(repairEntityFrom(repair1))
        repairDao.insert(repairEntityFrom(repair2))
        repairDao.insert(repairEntityFrom(repair3))
        repairDao.insert(repairEntityFrom(repair4))


        Log.d(TAG, "TESTING: testing room size cars is: ${carDao.getAll().size}")
        Log.d(TAG, "TESTING: testing room size parts is: ${partDao.getAll().size}")
        Log.d(TAG, "TESTING: testing room size notes is: ${noteDao.getAll().size}")
//                Log.d(TAG, "testing room car with $id1 size parts is: ${partService.readAllForCar(id1).value?.size}")
//                Log.d(TAG, "testing room car with $id2 size parts is: ${partService.readAllForCar(id2).value?.size}")
//                Log.d(TAG, "testing room size parts is: ${partService.readAll().value?.size}")

//                val carWithElem = carService.read(id1)
//                Log.d(TAG, "cars elements $id1 ${carWithElem?.partEntities?.size}")
//                Log.d(TAG, "cars elements $id1 ${carWithElem?.noteEntities?.size}")
//                Log.d(TAG, "cars elements $id1 ${carWithElem?.repairEntities?.size}")

        Log.d(TAG, "TESTING: operation is finished.")
    }

//    override fun getPartsListForBuying(): List<String> {
//        val list: MutableList<String> = mutableListOf()
//
//        list.add("$car.id")
//        list.add("${car.brand} ${car.model} (${car.number}):")
//        getParts().forEach {
//            val line = PartServiceImpl(context, it).getLineForBuyList()
//            if (line.isNotEmpty()) list.add(line)
//        }
//        return list
//    }
//
//    override fun getTasksListForService(): List<String> {
//        val list: MutableList<String> = mutableListOf()
//
//        list.add("$car.id")
//        list.add("${car.brand} ${car.model} (${car.number}):")
//        getParts().forEach {
//            val line = PartServiceImpl(context, it).getLineForService()
//            if (line.isNotEmpty()) list.add(line)
//        }
//        return list
//    }
//
//    override fun getMileageAsLine(): String {
//        return "${car.brand} ${car.model} ${car.number}: ${car.mileage}km"
//    }
//
//    override fun getDataForMileageList(): List<String> {
//        val list: MutableList<String> = mutableListOf()
//        list.add("${car.brand} ${car.model} (${car.number}):")
//        list.add("\t ${car.mileage} km")
//        return list
//    }
//

//
//    override fun setPhotoFor(imageView: ImageView) {
////        TODO("not implemented")
//    }
//
//    override fun setConditionImageFor(imageView: ImageView) {
////        TODO("not implemented")
//    }
//
//    override fun isOverRide(): Boolean {
//        val parts = getParts()
//        if (parts.isEmpty()) return false
//
//        parts.listIterator().forEach {
//            if (PartServiceImpl(context, it).isOverRide()) return true
//        }
//        return false
//    }
//
//    override fun isNeedInspectionControl(): Boolean {
//        val parts = getParts()
//        if (parts.isEmpty()) return false
//
//        parts.listIterator().forEach {
//            if (PartServiceImpl(context, it).isNeedToInspection()) return true
//        }
//        return false
//    }
//
//    override fun isHasImportantNotes(): Boolean {
//        val notes = getNotes()
//        if (notes.isEmpty()) return false
//
//        notes.listIterator().forEach {
//            if (NoteServiceImpl(context, it).isHighImportant()) return true
//        }
//        return false
//    }
//
//    override fun isNeedService(): Boolean {
//        val parts = getParts()
//        if (parts.isEmpty()) return false
//
//        parts.listIterator().forEach {
//            if (PartServiceImpl(context, it).isNeedToService()) return true
//        }
//        return false
//    }
//
//    override fun isNeedCorrectOdometer(): Boolean {
//        return ChronoUnit.DAYS.between(
//            car.whenMileageRefreshed, LocalDate.now()
//        ).toInt() > howMuchDaysBeetweenCorrectOdo
//    }
//
//    override fun toStringBrandAndModel() = "${car.brand} ${car.model}"
}