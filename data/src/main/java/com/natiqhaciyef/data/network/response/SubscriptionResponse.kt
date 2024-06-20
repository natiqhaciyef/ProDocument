package com.natiqhaciyef.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubscriptionResponse(
    @SerializedName("title")
    val title: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("period")
    val perTime: Int,
    @SerializedName("time")
    val timeType: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("features")
    val features: List<String>,
    @SerializedName("expiration")
    val expireDate: String,
    @SerializedName("templateColor")
    val backgroundColor: String,
    @SerializedName("size")
    val size: Int,
    @SerializedName("sizeType")
    val sizeType: String,
    @SerializedName("premiumToken")
    val token: String
): Parcelable