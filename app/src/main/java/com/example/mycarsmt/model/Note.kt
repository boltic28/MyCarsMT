package com.example.mycarsmt.model

import com.example.mycarsmt.model.enums.NoteLevel
import java.time.LocalDate

class Note() {

    constructor(
        id: Long,
        carId: Long,
        partId: Long,
        description: String,
        date: LocalDate,
        importantLevel: NoteLevel
    ) : this() {
        this.id = id
        this.carId = carId
        this.partId = partId
        this.description = description
        this.date = date
        this.importantLevel = importantLevel
    }

    constructor(
        id: Long,
        carId: Long,
        mileage: Int,
        partId: Long,
        description: String,
        date: LocalDate,
        importantLevel: NoteLevel
    ) : this() {
        this.id = id
        this.carId = carId
        this.mileage = mileage
        this.partId = partId
        this.description = description
        this.date = date
        this.importantLevel = importantLevel
    }

    var id: Long = 0
    var carId: Long = 0
    var mileage: Int = 0
    var partId: Long = 0
    var description: String = "some note"
    var date: LocalDate = LocalDate.now()
    var importantLevel: NoteLevel = NoteLevel.INFO
}