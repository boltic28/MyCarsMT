package com.example.mycarsmt.model.database.vconvertors

import androidx.room.TypeConverter
import com.example.mycarsmt.model.enums.NoteLevel

class NoteLevelConverter {

    @TypeConverter
    fun fromInfoLevel(noteLevel: NoteLevel): Int {
        return noteLevel.value
    }

    @TypeConverter
    fun toInfoLevel(noteLevel: Int): NoteLevel? {
        return NoteLevel.fromInt(noteLevel)
    }
}