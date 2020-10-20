package com.example.mycarsmt.datalayer.vconvertors

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long): LocalDate {
        return LocalDate.ofEpochDay(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate): Long {
        return date.toEpochDay()
    }
}