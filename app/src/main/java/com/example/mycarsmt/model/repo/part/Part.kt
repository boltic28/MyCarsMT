package com.example.mycarsmt.model.repo.part

import androidx.room.*
import com.example.mycarsmt.model.repo.car.Car
import com.example.mycarsmt.model.repo.LocalDateConverter
import java.time.LocalDate

@Entity(foreignKeys = [
    ForeignKey(entity = Car::class, parentColumns = ["id"], childColumns = ["carId"])
])
class Part {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var carId: Long = 0

    var mileage: Int = 0
    var name: String = ""
    var codes: String = ""

    @ColumnInfo(name = "limit_km")
    var limitKM: Int = 0

    @ColumnInfo(name = "limit_day")
    var limitDays: Int = 0

    @ColumnInfo(name = "last_change_time")
    @TypeConverters(LocalDateConverter::class)
    var dateLastChange: LocalDate = LocalDate.now()

    @ColumnInfo(name = "last_change_km")
    var mileageLastChange: Int = 0

    var description: String = ""
    var photo = ""

    var type: String = ""
}