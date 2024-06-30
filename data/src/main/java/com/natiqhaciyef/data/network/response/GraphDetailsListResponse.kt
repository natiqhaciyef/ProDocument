package com.natiqhaciyef.data.network.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GraphDetailsListResponse(
    val details : List<GraphDetailModel>
) : Parcelable

@Parcelize
data class GraphDetailModel(
    val title: String,
    val type: String,
    val percentage: Double,
    val backgroundColor: String
): Parcelable

