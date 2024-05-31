package com.natiqhaciyef.data.network.response

import android.os.Parcelable
import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.common.model.payment.MappedSubscriptionPlanPaymentDetails
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentChequeModel(
    var checkId: String,
    var title: String,
    var description: String,
    var subscriptionDetails: SubscriptionPlanPaymentDetails,
    var totalAmount: Double,
    var currency: String,
    var paymentDetails: PaymentDetails,
) : Parcelable

@Parcelize
data class SubscriptionPlanPaymentDetails(
    var expirationTime: Int,
    var expirationTimeType: String,
    var price: Double,
    var fee: Double,
    var discount: Double = 0.0,
): Parcelable

@Parcelize
data class ChequePayloadModel(var payload: String) : Parcelable

@Parcelize
data class PaymentPickModel(
    var type: String,
    var image: Int,
    var maskedCardNumber: String = ""
): Parcelable