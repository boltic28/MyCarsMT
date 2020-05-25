package com.example.mycarsmt.model

import com.example.mycarsmt.model.enums.Condition
import java.io.Serializable
import java.time.LocalDate

class Car(): Serializable {

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

    var brand: String = ""
    var model: String = ""
    var number: String = ""
    var vin: String = ""
    var year: Int = 1980
    var mileage: Int = 0
    var whenMileageRefreshed: LocalDate = LocalDate.now()
    var condition: List<Condition> = listOf(Condition.OK)

    var photo: String = ""

    lateinit var parts: List<Part>
    lateinit var notes: List<Note>
    lateinit var repairs: List<Repair>

}