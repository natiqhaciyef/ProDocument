package com.natiqhaciyef.data.network.request

import android.os.Parcelable
import com.natiqhaciyef.data.network.response.SubscriptionPlanPaymentDetails
import kotlinx.parcelize.Parcelize

@Parcelize
data class QrCodeRequest(
    var qrCode: String,
    var subscriptionDetails: SubscriptionPlanPaymentDetails
): Parcelable