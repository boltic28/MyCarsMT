package com.example.mycarsmt.datalayer.data.note

import com.example.mycarsmt.datalayer.enums.NoteLevel
import java.time.LocalDate

data class NoteWithMileage(

    val id: Long,
    val carId: Long,
    val mileage: Int,
    val partId: Long,
    val description: String,
    val date: LocalDate = LocalDate.now(),
    val importantLevel: NoteLevel = NoteLevel.INFO
)
