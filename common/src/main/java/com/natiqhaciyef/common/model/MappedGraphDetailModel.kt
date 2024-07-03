package com.natiqhaciyef.common.model

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class MappedGraphDetailModel(
    val title: String,
    @DrawableRes val icon: Int,
    val percentage: String,
    @ColorRes val backgroundColor: Int
): Parcelable