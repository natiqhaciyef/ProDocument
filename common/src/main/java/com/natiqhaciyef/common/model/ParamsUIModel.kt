package com.natiqhaciyef.common.model

import androidx.annotation.DrawableRes


data class ParamsUIModel(
    val title: String,
    val fieldType: FieldType,
    val isAvailableEveryone: Boolean = true,
    @DrawableRes var fieldIcon: Int? = null
)