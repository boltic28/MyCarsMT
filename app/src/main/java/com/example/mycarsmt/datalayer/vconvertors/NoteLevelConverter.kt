package com.example.mycarsmt.datalayer.vconvertors

import androidx.room.TypeConverter
import com.example.mycarsmt.datalayer.enums.NoteLevel

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