package com.natiqhaciyef.prodocument.ui.view.main.profile.model

import androidx.annotation.DrawableRes
import com.natiqhaciyef.common.R


data class ParamsUIModel(
    val title: String,
    val fieldType: FieldType,
    val isAvailableEveryone: Boolean = true,
    @DrawableRes var fieldIcon: Int? = null
)