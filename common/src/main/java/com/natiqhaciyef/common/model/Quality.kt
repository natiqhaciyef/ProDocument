package com.natiqhaciyef.common.model

enum class Quality {
    HIGH,
    MEDIUM,
    LOW,
    STANDARD;

    companion object{
        fun convertByName(name: String): Quality = when(name){
            "High", "HIGH", "high" -> { HIGH }
            "Medium", "MEDIUM", "medium" -> { MEDIUM }
            "Low", "LOW", "low" -> { LOW }
            else -> { STANDARD }
        }

    }
}