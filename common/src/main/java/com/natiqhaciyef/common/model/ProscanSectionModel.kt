package com.natiqhaciyef.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProscanSectionModel(
    val title: String,
    val link: String
): Parcelable