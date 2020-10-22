package com.example.mycarsmt.businesslayer

import java.io.Serializable
import java.time.LocalDate

class Repair(): Serializable {

    constructor(
        id: Long,
        carId: Long,
        partId: Long,
        type: String,
        cost: Int,
        mileage: Int,
        description: String,
        date: LocalDate
    ) : this() {
        this.id = id
        this.carId = carId
        this.partId = partId
        this.type = type
        this.cost = cost
        this.mileage = mileage
        this.description = description
        this.date = date
    }

    var id: Long = 0
    var carId: Long = 0
    var partId: Long = 0
    var type: String = "type"
    var cost: Int = 0
    var mileage: Int = 0
    var description: String = "some repair"
    var date: LocalDate = LocalDate.now()
}