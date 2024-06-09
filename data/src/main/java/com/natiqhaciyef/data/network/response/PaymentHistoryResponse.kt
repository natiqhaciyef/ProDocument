package com.natiqhaciyef.data.network.response

import android.os.Parcelable
import com.natiqhaciyef.common.model.payment.PaymentDetails
import kotlinx.parcelize.Parcelize


@Parcelize
data class PaymentHistoryResponse(
    var chequeId: String,
    var title: String,
//    var
    var paymentDetails: PaymentDetails
): Parcelable