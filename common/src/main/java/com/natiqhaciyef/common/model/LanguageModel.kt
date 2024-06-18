package com.natiqhaciyef.common.model

data class LanguageModel(
    val title: String,
    val detailedName: String,
    val isSelected: Boolean,
    val flagId: Int = 0,
)