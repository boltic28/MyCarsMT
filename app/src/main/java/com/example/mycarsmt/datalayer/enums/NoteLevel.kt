package com.example.mycarsmt.datalayer.enums

enum class NoteLevel(val value: Int) {
    INFO(1),
    LOW(2),
    MIDDLE(3),
    HIGH(4);

    companion object {
        private val map = values().associateBy(NoteLevel::value)
        fun fromInt(type: Int) = map[type]?: INFO
    }
}