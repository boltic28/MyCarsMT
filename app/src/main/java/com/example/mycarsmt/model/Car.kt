package com.example.mycarsmt.model

import com.example.mycarsmt.model.enums.CarCondition
import java.time.LocalDate

class Car() {

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
        condition: List<CarCondition>
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

    var brand: String = ""
    var model: String = ""
    var number: String = ""
    var vin: String = ""
    var year: Int = 1980
    var mileage: Int = 0
    var whenMileageRefreshed: LocalDate = LocalDate.now()
    var condition: List<CarCondition> = listOf(CarCondition.OK)

    var photo: String = ""

    lateinit var parts: MutableList<Part>
    lateinit var notes: MutableList<Note>
    lateinit var repairs: MutableList<Repair>

}