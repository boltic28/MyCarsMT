package com.example.mycarsmt.businesslayer

import android.os.Parcelable
import com.example.mycarsmt.datalayer.enums.NoteLevel
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

@Parcelize
data class Note(

    val id: Long,
    val carId: Long,
    val mileage: Int,
    val partId: Long,
    val description: String,
    val date: LocalDate,
    val importantLevel: NoteLevel
) : Parcelable