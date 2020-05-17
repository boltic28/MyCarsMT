package com.example.mycarsmt.model.database.vconvertors

import androidx.room.TypeConverter
import com.example.mycarsmt.model.enums.CarCondition

class ConditionConverter {

    @TypeConverter
    fun fromCondition(condition: CarCondition): Int {
        return condition.value
    }

    @TypeConverter
    fun toCondition(condition: Int): CarCondition {
        return CarCondition.fromInt(condition)!!
    }
}