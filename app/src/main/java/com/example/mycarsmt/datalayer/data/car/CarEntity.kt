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

    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val brand: String,
    val model: String,
    val number: String,
    val vin: String,
    val photo: String,
    val year: Int,
    val mileage: Int,

    @ColumnInfo(name = "mileage_changed_at")
    @TypeConverters(LocalDateConverter::class)
    val whenMileageRefreshed: LocalDate = LocalDate.now(),

    @TypeConverters(ConditionConverter::class)
    val condition: List<Condition> = listOf(Condition.OK)
)