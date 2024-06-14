package com.natiqhaciyef.common.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountSettingModel(
    @DrawableRes val image: Int,
    val title: String
): Parcelable
