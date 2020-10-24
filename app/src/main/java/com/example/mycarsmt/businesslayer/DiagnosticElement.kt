package com.example.mycarsmt.businesslayer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DiagnosticElement(val car: Car, val list: List<String>) : Parcelable