package com.example.mycarsmt.datalayer.data.note

import com.example.mycarsmt.datalayer.enums.NoteLevel
import java.time.LocalDate

data class NoteWithMileage(

    val id: Long,
    var carId: Long,
    var mileage: Int,
    var partId: Long,
    var description: String,
    var date: LocalDate = LocalDate.now(),
    var importantLevel: NoteLevel = NoteLevel.INFO
)
