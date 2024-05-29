package com.natiqhaciyef.common.model.ui

enum class SubscriptionType {
    BEGINNER,
    ADVANCED,
    PARTNER,
    OTHER,
    CUSTOM;

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