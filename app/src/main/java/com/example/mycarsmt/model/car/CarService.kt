package com.example.mycarsmt.model.car

import android.widget.ImageView

interface CarService {

    fun create()
    fun update()
    fun delete()

    fun getPartsListForBuying(): List<String>
    fun getTasksListForService(): List<String>
    fun getMileageAsLine(): String
    fun getDataForMileageList(): List<String>
    fun getCountOfNotes(): Int?

    fun setPhotoFor(imageView: ImageView)
    fun setConditionImageFor(imageView: ImageView)

    fun isOverRide(): Boolean
    fun isNeedInspectionControl(): Boolean
    fun isHasImportantNotes(): Boolean
    fun isNeedAnyService(): Boolean
    fun isNeedCorrectOdometer(): Boolean

    fun writeHistoryToFile(): String

    fun toStringHistoryOfRepairs(): String
    fun toStringForDB(): String
    fun toStringBrandAndModel(): String
}