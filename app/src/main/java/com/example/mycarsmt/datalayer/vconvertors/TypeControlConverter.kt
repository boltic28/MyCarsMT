package com.example.mycarsmt.datalayer.vconvertors

import androidx.room.TypeConverter
import com.example.mycarsmt.datalayer.enums.PartControlType

class TypeControlConverter {

    @TypeConverter
    fun fromTypeControl(controlType: PartControlType): String {
        return controlType.value
    }

    @TypeConverter
    fun toTypeControl(controlType: String): PartControlType? {
        return PartControlType.fromString(controlType)
    }
}