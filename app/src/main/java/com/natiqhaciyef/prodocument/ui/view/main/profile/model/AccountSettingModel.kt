package com.natiqhaciyef.prodocument.ui.view.main.profile.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountSettingModel(
    @DrawableRes val image: Int,
    val type: Settings
): Parcelable
