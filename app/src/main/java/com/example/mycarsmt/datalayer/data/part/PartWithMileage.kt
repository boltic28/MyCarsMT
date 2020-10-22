package com.example.mycarsmt.datalayer.data.part

import com.example.mycarsmt.datalayer.enums.Condition
import com.example.mycarsmt.datalayer.enums.PartControlType
import java.time.LocalDate

data class PartWithMileage(

    val id: Long,
    val carId: Long,
    val mileage: Int,
    val name: String,
    val codes: String,
    val limitKM: Int,
    val limitDays: Int,
    val dateLastChange: LocalDate,
    val mileageLastChange: Int,
    val description: String,
    val photo: String,
    val type: PartControlType,
    val condition: List<Condition>
)