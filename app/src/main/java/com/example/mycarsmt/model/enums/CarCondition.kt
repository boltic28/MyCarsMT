package com.example.mycarsmt.model.enums

enum class CarCondition(val value: Int) {
    OK(1),
    MAKE_INSPECTION(2),
    BUY_PARTS(3),
    MAKE_SERVICE(4),
    ATTENTION(5),
    CONTINUE_SMT(0);

    companion object {
        private val map = values().associateBy(CarCondition::value)
        fun fromInt(type: Int) = map[type]
    }
}