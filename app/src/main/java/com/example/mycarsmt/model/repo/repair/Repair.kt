package com.example.mycarsmt.model.repo.repair

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mycarsmt.model.repo.LocalDateConverter
import com.example.mycarsmt.model.repo.car.Car
import com.example.mycarsmt.model.repo.part.Part
import java.time.LocalDate

@Entity(foreignKeys = [
ForeignKey(entity = Car::class, parentColumns = ["id"], childColumns = ["carId"]),
ForeignKey(entity = Part::class, parentColumns = ["id"], childColumns = ["partId"])
])
class Repair {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var carId: Int = 0
    var partId: Int = 0

    var type: String = ""
    var cost: Double = 0.0
    var description: String = ""

    @TypeConverters(LocalDateConverter::class)
    var date: LocalDate = LocalDate.now()
    var mileage: Int = 0

    var partName: String = ""
}