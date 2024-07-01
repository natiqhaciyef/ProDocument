package com.natiqhaciyef.domain.network.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QrPaymentResponse(
    var merchantId: Int,
    var paymentType: String,
    var paymentMethod: String,
    var cheque: PaymentChequeResponse
) : Parcelable