package com.example.mycarsmt.data.vconvertors

import androidx.room.TypeConverter
import com.example.mycarsmt.data.enums.Condition
import java.lang.StringBuilder

class ConditionConverter {

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
}