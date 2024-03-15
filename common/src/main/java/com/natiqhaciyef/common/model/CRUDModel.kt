package com.natiqhaciyef.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CRUDModel(
    val resultCode: Int,
    val message: String,
): Parcelable