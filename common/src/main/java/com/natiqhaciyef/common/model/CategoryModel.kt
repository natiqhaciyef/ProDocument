package com.natiqhaciyef.common.model

import androidx.annotation.ColorRes
import com.natiqhaciyef.common.R

data class CategoryModel(
    val title: String,
    val details: String? = null,
    @ColorRes val backgroundColor: Int = R.color.white
)