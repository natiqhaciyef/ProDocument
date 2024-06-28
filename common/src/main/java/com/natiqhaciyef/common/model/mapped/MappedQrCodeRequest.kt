package com.natiqhaciyef.common.model.mapped

import android.os.Parcelable
import com.natiqhaciyef.common.model.payment.MappedSubscriptionPlanPaymentDetails
import kotlinx.parcelize.Parcelize

@Parcelize
data class MappedQrCodeRequest(
    var qrCode: String,
    var subscriptionPayment: MappedSubscriptionPlanPaymentDetails
) : Parcelable
