package com.example.mycarsmt.model.database.vconvertors

import androidx.room.TypeConverter
import com.example.mycarsmt.model.enums.CarCondition
import java.lang.StringBuilder
import java.util.*
import java.util.function.Function
import java.util.stream.Collector
import java.util.stream.Collectors
import java.util.stream.Stream

class ConditionConverter {

    @TypeConverter
    fun fromCondition(condition: List<CarCondition>): String {
        val line = StringBuilder()
        condition.forEach {
            line.append("${it.value},")
        }
        line.deleteCharAt(line.lastIndex)
        return line.toString()
    }

    @TypeConverter
    fun toCondition(condition: String): List<CarCondition>? {
        val result: MutableList<CarCondition> = mutableListOf()

        condition.split(',').forEach {
            if (it.isNotEmpty()) result.add(CarCondition.fromString(it)!!)
            }

        return result
    }
}