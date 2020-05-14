package com.example.mycarsmt.model.part

import android.widget.ImageView
import com.example.mycarsmt.model.repo.repair.Repair

interface PartService{

    fun create()
    fun read()
    fun update()
    fun delete()

    fun getMileageToRepair(): Int
    fun getUsedMileage(): Int
    fun getDaysToRepair(): Int
    fun getPhotoFor(imageView: ImageView)
    fun getInfoToChange(): String
    fun getLineForBuyList(): String
    fun getRepairs(): List<Repair>

    fun isNeedToBuy(): Boolean
    fun isNeedToService(): Boolean
    fun isNeedToInspection(): Boolean
    fun isOverRide(): Boolean

    fun makeService()
    fun getLineForService(): String

    fun getConditionImageFor(imageView: ImageView)

    fun toStringForDB(): String
}