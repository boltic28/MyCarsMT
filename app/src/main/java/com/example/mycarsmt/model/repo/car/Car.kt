package com.example.mycarsmt.model.repo.car

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mycarsmt.model.CarCondition
import com.example.mycarsmt.model.repo.ConditionConverter
import com.example.mycarsmt.model.repo.LocalDateConverter
import java.time.LocalDate

@Entity
open class Car {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var brand: String = ""
    var model: String = ""
    var number: String = ""
    var vin: String = ""
    var photo: String = ""

    var year: Int = 1980
    var mileage: Int = 0

    @ColumnInfo(name = "mileage_changed_at")
    @TypeConverters(LocalDateConverter::class)
    var whenMileageRefreshed: LocalDate = LocalDate.now()
    @TypeConverters(ConditionConverter::class)
    var condition: CarCondition = CarCondition.OK
}