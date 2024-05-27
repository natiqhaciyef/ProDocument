package com.natiqhaciyef.common.model.mapped

import android.os.Parcelable
import com.natiqhaciyef.common.model.Color
import com.natiqhaciyef.common.model.SubscriptionType
import com.natiqhaciyef.common.model.Time
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
    val expireDate: String,
    val token: String
): Parcelable