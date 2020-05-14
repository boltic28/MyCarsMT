package com.example.mycarsmt.model.car

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import com.example.mycarsmt.model.repo.AppDatabase
import com.example.mycarsmt.model.repo.car.Car
import com.example.mycarsmt.model.repo.note.Note
import com.example.mycarsmt.model.repo.part.Part
import com.example.mycarsmt.model.repo.repair.Repair
import java.util.*

@SuppressLint("NewApi")
class CarServiceImpl(val context: Context, val car: Car) : CarService {

    val db = AppDatabase.getAppDataBase(context)
    val carDao = db?.carDao()
    val partDao = db?.partDao()
    val noteDao = db?.noteDao()
    val repairDao = db?.repairDao()

    override fun create() {
        carDao?.insert(car)
    }

    override fun update() {
        carDao?.update(car)
    }

    override fun delete() {
        carDao?.delete(car)
    }

    override fun getPartsListForBuying(): List<String> {
        val partsList = partDao?.getAllForCar(car.id)
        return emptyList()
    }

    override fun getTasksListForService(): List<String> {
        val partsList = partDao?.getAllForCar(car.id)
        return emptyList()
    }

    override fun getMileageAsLine(): String {
        return "${car.brand} ${car.model} ${car.number}: ${car.mileage}km"
    }

    override fun getDataForMileageList(): List<String> {
        return emptyList()
    }

    override fun getCountOfNotes(): Int? = noteDao?.getAllForCar(car.id)?.size

    override fun setPhotoFor(imageView: ImageView) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setConditionImageFor(imageView: ImageView) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isOverRide(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isNeedInspectionControl(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isHasImportantNotes(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isNeedAnyService(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isNeedCorrectOdometer(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeHistoryToFile(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toStringHistoryOfRepairs(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toStringForDB(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toStringBrandAndModel(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}