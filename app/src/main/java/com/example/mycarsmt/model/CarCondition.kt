package com.example.mycarsmt.model

enum class CarCondition(val value: String) {
    OK("ok"),
    MAKE_INSPECTION("insp"),
    BUY_PARTS("buy"),
    MAKE_SERVICE("service"),
    ATTENTION("attention"),
    CONTINUE_SMT("continue")
}