package com.natiqhaciyef.common.model.payment

import android.os.Parcelable
import com.natiqhaciyef.common.model.Currency
import kotlinx.parcelize.Parcelize

@Parcelize
data class MappedPaymentChequeModel(
    var checkId: String,
    var title: String,
    var description: String? = null,
    var subscriptionDetails: MappedSubscriptionPlanPaymentDetails,
    var totalAmount: Double,
    var currency: Currency,
    var paymentDetails: PaymentDetails,
    var paymentResult: PaymentResultType
): Parcelable

