package com.natiqhaciyef.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProScanInfoModel(
    val title: String,
    val description: String,
    val icon: String,
    val version: String
): Parcelable
