package com.example.mycarsmt.model.database.note

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import com.example.mycarsmt.model.enums.NoteLevel
import com.example.mycarsmt.model.database.vconvertors.LocalDateConverter
import com.example.mycarsmt.model.database.vconvertors.NoteLevelConverter
import java.time.LocalDate

class NoteWithMileage() {

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

    @ColumnInfo(name = "car_id")
    var carId: Long = 0

    var mileage: Int = 0
    @ColumnInfo(name = "part_id")
    var partId: Long = 0

    var description: String = ""

    @TypeConverters(LocalDateConverter::class)
    var date: LocalDate = LocalDate.now()

    @ColumnInfo(name = "important_level")
    @TypeConverters(NoteLevelConverter::class)
    var importantLevel: NoteLevel = NoteLevel.INFO
}