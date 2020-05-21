package com.example.mycarsmt.model

import android.widget.ImageView

interface CarInterface {

    fun getPartsListForBuying(car: Car): List<String>
    fun getTasksListForService(car: Car): List<String>
    fun getMileageAsLine(car: Car): String
    fun getDataForMileageList(car: Car): List<String>
    fun getCountOfNotes(car: Car): Int?

    fun setPhotoFor(imageView: ImageView)
    fun setConditionImageFor(imageView: ImageView)

    fun isOverRide(car: Car): Boolean
    fun isNeedInspectionControl(car: Car): Boolean
    fun isHasImportantNotes(car: Car): Boolean
    fun isNeedService(car: Car): Boolean
    fun isNeedCorrectOdometer(car: Car): Boolean

    fun toStringBrandAndModel(car: Car): String
}