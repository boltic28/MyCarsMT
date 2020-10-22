package com.example.mycarsmt.datalayer.data

import androidx.room.TypeConverter
import com.example.mycarsmt.datalayer.enums.Condition
import com.example.mycarsmt.datalayer.enums.NoteLevel
import com.example.mycarsmt.datalayer.enums.PartControlType
import java.lang.StringBuilder
import java.time.LocalDate


class Converter {

    @TypeConverter
    fun fromCondition(condition: List<Condition>): String {
        val line = StringBuilder()
        condition.forEach {
            line.append("${it.value},")
        }
        line.deleteCharAt(line.lastIndex)
        return line.toString()
    }

    @TypeConverter
    fun toCondition(condition: String): List<Condition>? {
        val result: MutableList<Condition> = mutableListOf()

        condition.split(',').forEach {
            if (it.isNotEmpty()) result.add(Condition.fromString(it)!!)
        }

        return result
    }

    @TypeConverter
    fun fromTimestamp(value: Long): LocalDate {
        return LocalDate.ofEpochDay(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate): Long {
        return date.toEpochDay()
    }

    @TypeConverter
    fun fromInfoLevel(noteLevel: NoteLevel): Int {
        return noteLevel.value
    }

    @TypeConverter
    fun toInfoLevel(noteLevel: Int): NoteLevel {
        return NoteLevel.fromInt(noteLevel)
    }

    @TypeConverter
    fun fromTypeControl(controlType: PartControlType): String {
        return controlType.value
    }

    @TypeConverter
    fun toTypeControl(controlType: String): PartControlType {
        return PartControlType.fromString(controlType)
    }
}