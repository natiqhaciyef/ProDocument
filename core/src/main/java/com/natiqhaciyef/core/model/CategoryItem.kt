package com.natiqhaciyef.core.model

data class CategoryItem(
    val id: Int,
    val title: String,
    val iconId: Int,
    val type: String,
    val size: Double? = null,
    val sizeType: String? = null
)
