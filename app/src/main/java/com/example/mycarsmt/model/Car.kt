package com.example.mycarsmt.model

import com.example.mycarsmt.SpecialWords
import com.example.mycarsmt.model.enums.Condition
import java.io.Serializable
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Car(): Serializable {

    val howMuchDaysBeetweenCorrectOdo = 15

    constructor(
        id: Long,
        brand: String,
        model: String,
        number: String,
        vin: String,
        photo: String,
        year: Int,
        mileage: Int,
        whenMileageRefreshed: LocalDate,
        condition: List<Condition>
    ) : this() {
        this.id = id
        this.brand = brand
        this.model = model
        this.number = number
        this.vin = vin
        this.photo = photo
        this.year = year
        this.mileage = mileage
        this.whenMileageRefreshed = whenMileageRefreshed
        this.condition = condition
    }

    var id: Long = 0

    var brand: String = "brand"
    var model: String = "model"
    var number: String = "0000 AA-7"
    var vin: String = "VIN CODE"
    var year: Int = 1980
    var mileage: Int = 0
    var whenMileageRefreshed: LocalDate = LocalDate.now()
    var condition: List<Condition> = listOf(Condition.OK)

    var photo: String = SpecialWords.NO_PHOTO.value

    lateinit var parts: List<Part>
    lateinit var notes: List<Note>
    lateinit var repairs: List<Repair>

    fun checkConditions(){
        val list = mutableListOf<Condition>()
        if (isNeedInspectionControl()) list.add(Condition.MAKE_INSPECTION)
        if (isNeedToBuy()) list.add(Condition.BUY_PARTS)
        if (isNeedService()) list.add(Condition.MAKE_SERVICE)
        if (isOverRide()) list.add(Condition.ATTENTION)
        if (isNeedCorrectOdometer()) list.add(Condition.CHECK_MILEAGE)
        if (list.isEmpty()) list.add(Condition.OK)
        condition = list
    }

    private fun isOverRide(): Boolean {
        if (parts.isEmpty()) return false

        parts.listIterator().forEach {
            if (it.isOverRide()) return true
        }
        return false
    }

    private fun isNeedInspectionControl(): Boolean {
        if (parts.isEmpty()) return false

        parts.listIterator().forEach {
            if (it.isNeedToInspection()) return true
        }
        return false
    }

    private fun isNeedService(): Boolean {
        if (parts.isEmpty()) return false

        parts.listIterator().forEach {
            if (it.isNeedToService()) return true
        }
        return false
    }

    private fun isNeedToBuy(): Boolean {
        if (parts.isEmpty()) return false

        parts.listIterator().forEach {
            if (it.isNeedToBuy()) return true
        }
        return false
    }

    private fun isNeedCorrectOdometer(): Boolean {
        return ChronoUnit.DAYS.between(
            whenMileageRefreshed, LocalDate.now()
        ).toInt() > howMuchDaysBeetweenCorrectOdo
    }

    fun getFullName() = "$brand $model ($number)"

    fun getListToBuy(): List<String>{
        val list: MutableList<String> = mutableListOf()

        parts.forEach {
            val line = it.getLineForBuyList()
            if (line.isNotEmpty()) list.add(line)
        }
        return list
    }

    fun getListToDo(): List<String>{
        val list: MutableList<String> = mutableListOf()

        parts.forEach {
            val line = it.getLineForService()
            if (line.isNotEmpty()) list.add(line)
        }
        return list
    }

//   private fun isHasImportantNotes(): Boolean {
//        if (notes.isEmpty()) return false
//
//        notes.listIterator().forEach {
//            if (it.isHighImportant()) return true
//        }
//        return false
//    }

}