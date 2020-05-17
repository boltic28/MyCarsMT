package com.example.mycarsmt.model

import com.example.mycarsmt.model.enums.PartControlType
import java.time.LocalDate

class Part() {

    constructor(
        id: Long,
        carId: Long,
        mileage: Int,
        name: String,
        codes: String,
        limitKM: Int,
        limitDays: Int,
        dateLastChange: LocalDate,
        mileageLastChange: Int,
        description: String,
        photo: String,
        type: PartControlType
    ) : this() {
        this.id = id
        this.carId = carId
        this.mileage = mileage
        this.name = name
        this.codes = codes
        this.limitKM = limitKM
        this.limitDays = limitDays
        this.dateLastChange = dateLastChange
        this.mileageLastChange = mileageLastChange
        this.description = description
        this.photo = photo
        this.type = type
    }

    constructor(
        id: Long,
        carId: Long,
        name: String,
        codes: String,
        limitKM: Int,
        limitDays: Int,
        dateLastChange: LocalDate,
        mileageLastChange: Int,
        description: String,
        photo: String,
        type: PartControlType
    ) : this() {
        this.id = id
        this.carId = carId
        this.name = name
        this.codes = codes
        this.limitKM = limitKM
        this.limitDays = limitDays
        this.dateLastChange = dateLastChange
        this.mileageLastChange = mileageLastChange
        this.description = description
        this.photo = photo
        this.type = type
    }

    var id: Long = 0

    var carId: Long = 0
    var mileage: Int = 0
    var name: String = "part"
    var codes: String = "no23"
    var limitKM: Int = 10000
    var limitDays: Int = 365
    var dateLastChange: LocalDate = LocalDate.now()
    var mileageLastChange: Int = 0
    var description: String = "description"
    var photo = ""
    var type: PartControlType = PartControlType.CHANGE
}