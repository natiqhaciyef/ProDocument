package com.natiqhaciyef.data.mapper

import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.helpers.cardMasking
import com.natiqhaciyef.common.model.Currency
import com.natiqhaciyef.common.model.Time
import com.natiqhaciyef.common.model.payment.MappedPaymentChequeModel
import com.natiqhaciyef.common.model.payment.MappedSubscriptionPlanPaymentDetails
import com.natiqhaciyef.common.model.payment.PaymentHistoryModel
import com.natiqhaciyef.common.model.payment.PaymentResultType
import com.natiqhaciyef.data.R
import com.natiqhaciyef.data.network.response.PaymentChequeResponse
import com.natiqhaciyef.data.network.response.SubscriptionPlanPaymentDetails

fun PaymentChequeResponse.toMapped(): MappedPaymentChequeModel {
    return MappedPaymentChequeModel(
        checkId = this.checkId,
        title = this.title,
        description = this.description,
        subscriptionDetails = this.subscriptionDetails.toMapped(),
        totalAmount = this.totalAmount,
        currency = Currency.stringToType(this.currency),
        paymentDetails = this.paymentDetails,
        paymentResult = PaymentResultType.stringToPaymentResultType(this.paymentResult),
        date = this.date
    )
}

fun MappedPaymentChequeModel.toResponse(): PaymentChequeResponse {
    return PaymentChequeResponse(
        checkId = this.checkId,
        title = this.title,
        description = this.description ?: EMPTY_STRING,
        subscriptionDetails = this.subscriptionDetails.toResponse(),
        totalAmount = this.totalAmount,
        currency = this.currency.name,
        paymentDetails = this.paymentDetails,
        paymentResult = this.paymentResult.title,
        date = this.date
    )
}

fun SubscriptionPlanPaymentDetails.toMapped(): MappedSubscriptionPlanPaymentDetails{
    return MappedSubscriptionPlanPaymentDetails(
        expirationTime = this.expirationTime,
        expirationTimeType = Time.stringToTimeType(this.expirationTimeType),
        price = this.price,
        fee = this.fee,
        discount = this.discount
    )
}

fun MappedSubscriptionPlanPaymentDetails.toResponse(): SubscriptionPlanPaymentDetails{
    return SubscriptionPlanPaymentDetails(
        expirationTime = this.expirationTime,
        expirationTimeType = this.expirationTimeType.name,
        price = this.price,
        fee = this.fee,
        discount = this.discount
    )
}

fun PaymentChequeResponse.toPaymentHistory(): PaymentHistoryModel{
    val icon = when(this.subscriptionDetails.price){
        in 0.1 .. 1.0 -> { com.natiqhaciyef.common.R.drawable.color_blue_gradient }
        in 1.1 .. 5.0 -> { com.natiqhaciyef.common.R.drawable.color_red_gradient }
        in 5.1 .. 10.0 -> { com.natiqhaciyef.common.R.drawable.color_orange_gradient }
        in 10.1 .. 15.0 -> { com.natiqhaciyef.common.R.drawable.color_brown_gradient }
        in 15.1 .. 25.0 -> { com.natiqhaciyef.common.R.drawable.color_green_gradient }
        in 25.1 .. 40.0 -> { com.natiqhaciyef.common.R.drawable.color_teal_gradient }
        in 40.1 .. 80.0 -> { com.natiqhaciyef.common.R.drawable.color_pink_gradient }
        in 80.1 .. 150.0 -> { com.natiqhaciyef.common.R.drawable.color_purple_gradient }
        else -> { com.natiqhaciyef.common.R.drawable.color_yellow_gradient }
    }

    return PaymentHistoryModel(
        chequeId = this.checkId,
        icon = icon,
        title = this.title,
        maskedCardNumber = this.paymentDetails.cardNumber.cardMasking(),
        price = this.totalAmount,
        currency = Currency.stringToType(this.currency)
    )
}