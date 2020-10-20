package com.example.mycarsmt.datalayer.data.note

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mycarsmt.datalayer.enums.NoteLevel
import com.example.mycarsmt.datalayer.vconvertors.LocalDateConverter
import com.example.mycarsmt.datalayer.vconvertors.NoteLevelConverter
import java.time.LocalDate

class NoteWithMileage(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "car_id")
    var carId: Long,

    var mileage: Int,
    @ColumnInfo(name = "part_id")
    var partId: Long,

    var description: String,

    @TypeConverters(LocalDateConverter::class)
    var date: LocalDate = LocalDate.now(),

    @ColumnInfo(name = "important_level")
    @TypeConverters(NoteLevelConverter::class)
    var importantLevel: NoteLevel = NoteLevel.INFO
)
