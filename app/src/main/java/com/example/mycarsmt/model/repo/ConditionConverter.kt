package com.example.mycarsmt.model.repo

import androidx.room.TypeConverter
import com.example.mycarsmt.model.CarCondition

class ConditionConverter {

    @TypeConverter
    fun fromCondition(condition: CarCondition): String {
        return condition.value
    }

    @TypeConverter
    fun toCondition(condition: String): CarCondition? {
        return CarCondition.valueOf(condition)
    }
}