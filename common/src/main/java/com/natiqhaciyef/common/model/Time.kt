package com.natiqhaciyef.common.model

enum class Time(val title: String) {
    MINUTE("Minute"),
    HOUR("Hour"),
    DAY("Day"),
    WEEK("Week"),
    MONTH("Month"),
    YEAR("Year"),
    DECADE("Decade"),
    NON_OF_THEM("Non of them");

    companion object{
        fun stringToTimeType(time: String) = when(time.lowercase()){
            MINUTE.name, MINUTE.name.lowercase() -> { MINUTE }
            HOUR.name, HOUR.name.lowercase() -> { HOUR }
            DAY.name, DAY.name.lowercase() -> { DAY }
            WEEK.name, WEEK.name.lowercase() -> { WEEK }
            MONTH.name, MONTH.name.lowercase() -> { MONTH }
            YEAR.name, YEAR.name.lowercase() -> { YEAR }
            DECADE.name, DECADE.name.lowercase() -> { DECADE }

            else -> { NON_OF_THEM }
        }
    }
}