package com.natiqhaciyef.domain.network.response

import android.os.Parcelable
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.model.payment.PaymentDetails
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentChequeResponse(
    var checkId: String,
    var title: String,
    var description: String,
    var subscriptionDetails: SubscriptionPlanPaymentDetails,
    var totalAmount: Double,
    var currency: String,
    var paymentDetails: PaymentDetails,
    var paymentResult: String,
    var date: String
) : Parcelable

@Parcelize
data class SubscriptionPlanPaymentDetails(
    var expirationTime: Int,
    var expirationTimeType: String,
    var price: Double,
    var fee: Double,
    var discount: Double = ZERO.toDouble(),
): Parcelable

@Parcelize
data class ChequePayloadModel(var payload: String) : Parcelable

@Parcelize
data class PaymentPickModel(
    var type: String,
    var image: Int,
    var maskedCardNumber: String = EMPTY_STRING
): Parcelable