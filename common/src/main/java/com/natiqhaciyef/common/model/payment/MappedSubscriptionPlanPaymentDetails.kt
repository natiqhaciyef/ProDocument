package com.natiqhaciyef.common.model.payment

import android.os.Parcelable
import com.natiqhaciyef.common.model.Time
import kotlinx.parcelize.Parcelize

@Parcelize
data class MappedSubscriptionPlanPaymentDetails(
    var expirationTime: Int,
    var expirationTimeType: Time,
    var price: Double,
    var fee: Double,
    var discount: Double = 0.0,
): Parcelable