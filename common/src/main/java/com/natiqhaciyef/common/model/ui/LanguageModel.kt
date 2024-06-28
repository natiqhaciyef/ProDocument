package com.natiqhaciyef.common.model.ui

import com.natiqhaciyef.common.constants.ZERO

data class LanguageModel(
    val title: String,
    val detailedName: String,
    val isSelected: Boolean,
    val flagId: Int = ZERO,
)
