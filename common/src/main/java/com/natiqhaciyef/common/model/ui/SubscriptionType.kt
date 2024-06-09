package com.natiqhaciyef.common.model.ui

enum class SubscriptionType(val title: String) {
    BEGINNER("Beginner"),
    ADVANCED("Advanced"),
    PARTNER("Partner"),
    OTHER("Other"),
    CUSTOM("Custom");

    companion object {
        fun stringToSubscriptionType(title: String) = when (title.lowercase()) {
            BEGINNER.name, BEGINNER.name.lowercase() -> { BEGINNER }
            ADVANCED.name, ADVANCED.name.lowercase() -> { ADVANCED }
            PARTNER.name, PARTNER.name.lowercase() -> { PARTNER }
            OTHER.name, OTHER.name.lowercase() -> { OTHER }
            else -> { CUSTOM }
        }
    }
}