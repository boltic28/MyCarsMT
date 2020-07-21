package com.example.mycarsmt.data.database.part

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import com.example.mycarsmt.data.database.vconvertors.ConditionConverter
import com.example.mycarsmt.data.database.vconvertors.LocalDateConverter
import com.example.mycarsmt.data.database.vconvertors.TypeControlConverter
import com.example.mycarsmt.data.enums.Condition
import com.example.mycarsmt.data.enums.PartControlType
import java.time.LocalDate

class PartWithMileage() {

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
        type: PartControlType,
        condition: List<Condition>

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
        this.condition = condition
    }
    var id: Long = 0

    @ColumnInfo(name = "car_id")
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

    @TypeConverters(TypeControlConverter::class)
    var type: PartControlType = PartControlType.CHANGE

    @TypeConverters(ConditionConverter::class)
    var condition: List<Condition> = listOf(Condition.OK)
}