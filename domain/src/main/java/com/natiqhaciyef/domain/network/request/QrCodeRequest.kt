package com.natiqhaciyef.domain.network.request

import android.os.Parcelable
import com.natiqhaciyef.domain.network.response.SubscriptionPlanPaymentDetails
import kotlinx.parcelize.Parcelize

@Parcelize
data class QrCodeRequest(
    var qrCode: String,
    var subscriptionDetails: SubscriptionPlanPaymentDetails
): Parcelable