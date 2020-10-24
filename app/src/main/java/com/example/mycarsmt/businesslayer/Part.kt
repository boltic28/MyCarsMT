package com.example.mycarsmt.businesslayer

import android.os.Parcelable
import com.example.mycarsmt.datalayer.enums.Condition
import com.example.mycarsmt.datalayer.enums.PartControlType
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

@Parcelize
data class Part(

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
    val condition: List<Condition>,
    val notes: List<Note>,
    val repairs: List<Repair>
) : Parcelable

