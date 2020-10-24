package com.example.mycarsmt.businesslayer

import android.os.Parcelable
import com.example.mycarsmt.datalayer.enums.Condition
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

@Parcelize
data class Car(

    val id: Long,
    val brand: String,
    val model: String,
    val number: String,
    val vin: String,
    val year: Int,
    val mileage: Int,
    val whenMileageRefreshed: LocalDate,
    val condition: List<Condition>,
    val photo: String
) : Parcelable






