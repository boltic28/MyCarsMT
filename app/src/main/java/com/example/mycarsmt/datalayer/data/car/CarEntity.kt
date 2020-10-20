package com.example.mycarsmt.datalayer.data.car

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mycarsmt.datalayer.enums.Condition
import com.example.mycarsmt.datalayer.vconvertors.ConditionConverter
import com.example.mycarsmt.datalayer.vconvertors.LocalDateConverter
import java.time.LocalDate

@Entity(tableName = "car")
data class CarEntity(
    val id:Long,
    //to do with other fields
) {

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