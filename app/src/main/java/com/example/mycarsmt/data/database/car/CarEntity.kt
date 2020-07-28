package com.example.mycarsmt.data.database.car

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mycarsmt.data.enums.Condition
import com.example.mycarsmt.data.vconvertors.ConditionConverter
import com.example.mycarsmt.data.vconvertors.LocalDateConverter
import java.time.LocalDate

@Entity(tableName = "car")
open class CarEntity() {

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
    var condition: List<Condition> = listOf(Condition.OK)
}