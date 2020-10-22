package com.example.mycarsmt.datalayer.enums

enum class PartName(val value: String) {
    DEFAULT("default part"),
    OIL_LEVEL("oil level"),
    OIL_FILTER("oil filter"),
    AIR_FILTER("air filter"),
    CABIN_FILTER("cabin filter"),
    FUEL_FILTER("fuel filter"),
    FRONT_BRAKE("front brake"),
    REAR_BRAKE("rear brake"),
    GEARBOX("gearbox"),
    SPARK_PLUGS("spark plugs"),
    BATTERY("battery"),
    WIPERS("wipers"),
    INSURANCE("insurance"),
    TECH_VIEW("tech view"),
    ENGINE_BELT("belt");

    companion object {
        private val map = values().associateBy(PartName::value)
        fun fromString(type: String) = map[type]?: DEFAULT
    }

}