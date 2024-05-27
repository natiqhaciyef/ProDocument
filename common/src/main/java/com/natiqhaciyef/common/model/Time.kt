package com.natiqhaciyef.common.model

enum class Time {
    MINUTE,
    HOUR,
    DAY,
    WEEK,
    MONTH,
    YEAR,
    DECADE,
    NON_OF_THEM;

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