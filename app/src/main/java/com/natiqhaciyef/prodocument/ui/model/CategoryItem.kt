package com.natiqhaciyef.prodocument.ui.model

data class CategoryItem(
    val id: Int,
    val title: String,
    val iconId: Int,
    val size: Double? = null,
    val sizeType: String? = null
)