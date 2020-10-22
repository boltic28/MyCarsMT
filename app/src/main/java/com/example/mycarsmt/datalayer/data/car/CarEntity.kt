package com.example.mycarsmt.datalayer.data.car

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mycarsmt.datalayer.enums.Condition
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
    val whenMileageRefreshed: LocalDate = LocalDate.now(),
    val condition: List<Condition> = listOf(Condition.OK)
)