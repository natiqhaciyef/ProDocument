package com.natiqhaciyef.common.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

data class MappedGraphDetailModel(
    val title: String,
    @DrawableRes val icon: Int,
    val percentage: String,
    @ColorRes val backgroundColor: Int
)