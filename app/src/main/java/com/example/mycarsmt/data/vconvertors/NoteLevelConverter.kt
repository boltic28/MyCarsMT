package com.example.mycarsmt.data.vconvertors

import androidx.room.TypeConverter
import com.example.mycarsmt.data.enums.NoteLevel

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