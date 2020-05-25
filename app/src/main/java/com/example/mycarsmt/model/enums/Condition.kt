package com.example.mycarsmt.model.enums

enum class Condition(val value: String) {
    OK("ok"),
    MAKE_INSPECTION("insp"),
    BUY_PARTS("buy"),
    MAKE_SERVICE("serv"),
    ATTENTION("atten"),
    CONTINUE_SMT("cont"),
    CHECK_MILEAGE("mileage"),
    OVERUSED("overused");

    companion object {
        private val map = values().associateBy(Condition::value)
        fun fromString(type: String) = map[type]
    }
}