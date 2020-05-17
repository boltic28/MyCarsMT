package com.example.mycarsmt.model.repo.car

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.mycarsmt.model.Car
import com.example.mycarsmt.model.Note
import com.example.mycarsmt.model.Part
import com.example.mycarsmt.model.Repair
import com.example.mycarsmt.model.database.AppDatabase
import com.example.mycarsmt.model.database.car.CarEntity
import com.example.mycarsmt.model.repo.utils.EntityConverter.Companion.carEntityFrom
import com.example.mycarsmt.model.repo.utils.EntityConverter.Companion.carFrom
import com.example.mycarsmt.model.repo.utils.EntityConverter.Companion.noteFrom
import com.example.mycarsmt.model.repo.utils.EntityConverter.Companion.partFrom
import com.example.mycarsmt.model.repo.utils.EntityConverter.Companion.repairFrom
import java.util.concurrent.ExecutorService
import java.util.stream.Collectors


class CarServiceImpl(val context: Context) : CarService {

    private var db: AppDatabase = AppDatabase.getInstance(context)
    private val executorService: ExecutorService = db.getDatabaseExecutorService()!!
    private var carEntityListLiveData: LiveData<List<CarEntity>>? = null

    private val carDao = db.carDao()
    private val partDao = db.partDao()
    private val noteDao = db.noteDao()
    private val repairDao = db.repairDao()


    //settings
    val howMuchDaysBeetweenCorrectOdo = 15

    override fun readAll(): LiveData<List<Car>> {
        if (carEntityListLiveData == null) {
            carEntityListLiveData = carDao.getAllLive()
        }

        return Transformations.map(carEntityListLiveData!!){ list -> list.stream()
            .map { carEntity -> carFrom(carEntity)}.collect(Collectors.toList())}
    }

    override fun readById(id: Long): LiveData<Car> {
        return Transformations.map(carDao.getByIdLive(id)){ entity -> carFrom(entity)}
    }

    override fun create(car: Car): Long {
        executorService.execute { car.id = carDao.insert(carEntityFrom(car)) }
        return car.id
    }

    override fun update(car: Car): Long {
        executorService.execute { car.id = carDao.update(carEntityFrom(car)) }
        return car.id
    }

    override fun delete(car: Car): Long {
        executorService.execute { car.id = carDao.delete(carEntityFrom(car)) }
        return car.id
    }

    override fun getParts(car: Car): LiveData<List<Part>> {
        return Transformations.map(partDao.getAllForCarLive(car.id)){ list -> list.stream()
            .map { partEntity -> partFrom(partEntity)}.collect(Collectors.toList())}
    }

    override fun getNotes(car: Car): LiveData<List<Note>> {
        return Transformations.map(noteDao.getAllForCarLive(car.id)){ list -> list.stream()
            .map { noteEntity -> noteFrom(noteEntity)}.collect(Collectors.toList())}
    }

    override fun getRepairs(car: Car): LiveData<List<Repair>> {
        return Transformations.map(repairDao.getAllForCarLive(car.id)){ list -> list.stream()
            .map { repairEntity -> repairFrom(repairEntity)}.collect(Collectors.toList())}
    }

    //
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
//    override fun getCountOfNotes(): Int? = noteDao?.getAllForCar(car.id)?.size
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