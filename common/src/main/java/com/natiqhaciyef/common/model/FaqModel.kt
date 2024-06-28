package com.natiqhaciyef.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FaqModel(
    var title: String,
    var description: String,
    var category: String
): Parcelable
