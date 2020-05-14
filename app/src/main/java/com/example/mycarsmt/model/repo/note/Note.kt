package com.example.mycarsmt.model.repo.note

import androidx.room.*
import com.example.mycarsmt.model.repo.LocalDateConverter
import com.example.mycarsmt.model.repo.car.Car
import com.example.mycarsmt.model.repo.part.Part
import java.time.LocalDate

@Entity(foreignKeys = [
    ForeignKey(entity = Car::class, parentColumns = ["id"], childColumns = ["carId"]),
    ForeignKey(entity = Part::class, parentColumns = ["id"], childColumns = ["partId"])
])
class Note {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var carId: Long = 0
    var partId: Long = 0

    var description: String = ""

    @TypeConverters(LocalDateConverter::class)
    var date: LocalDate = LocalDate.now()

    @ColumnInfo(name = "important_level")
    var importantLevel = ""
}