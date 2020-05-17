package com.example.mycarsmt.model.enums

enum class PartControlType(val value: String) {
    CHANGE("change"),
    INSPECTION("inspection"),
    INSURANCE("insurance"),
    REPAIR("repair");

    companion object {
        private val map = values().associateBy(PartControlType::value)
        fun fromString(type: String) = map[type]
    }
}