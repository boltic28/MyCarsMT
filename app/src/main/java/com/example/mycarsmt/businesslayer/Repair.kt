package com.example.mycarsmt.businesslayer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

@Parcelize
data class Repair(

    val id: Long,
    val carId: Long,
    val partId: Long,
    val type: String,
    val cost: Int,
    val mileage: Int,
    val description: String,
    val date: LocalDate
) : Parcelable