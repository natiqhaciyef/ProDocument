package com.natiqhaciyef.common.model.mapped

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.natiqhaciyef.common.model.StorageSize
import com.natiqhaciyef.common.model.ui.Color
import com.natiqhaciyef.common.model.ui.SubscriptionType
import com.natiqhaciyef.common.model.Time
import io.grpc.Context.Storage
import kotlinx.parcelize.Parcelize

@Parcelize
data class MappedSubscriptionModel(
    val title: SubscriptionType,
    val price: Double,
    val perTime: Int,
    val timeType: Time,
    val description: String,
    val features: List<String>,
    val backgroundColor: Color,
    val size: Int = 1024,
    val sizeType: StorageSize = StorageSize.MB,
    val expireDate: String,
    val token: String
): Parcelable