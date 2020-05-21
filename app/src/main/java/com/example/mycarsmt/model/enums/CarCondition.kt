package com.example.mycarsmt.model.enums

enum class CarCondition(val value: String) {
    OK("ok"),
    MAKE_INSPECTION("insp"),
    BUY_PARTS("buy"),
    MAKE_SERVICE("serv"),
    ATTENTION("atten"),
    CONTINUE_SMT("cont");

    companion object {
        private val map = values().associateBy(CarCondition::value)
        fun fromString(type: String) = map[type]
    }
}