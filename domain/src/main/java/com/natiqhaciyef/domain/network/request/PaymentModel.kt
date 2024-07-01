package com.natiqhaciyef.domain.network.request

import android.os.Parcelable
import com.natiqhaciyef.common.model.payment.PaymentDetails
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentModel(
    var merchantId: Int,
    var paymentType: String,
    var paymentMethod: String,
    var paymentDetails: PaymentDetails
): Parcelable