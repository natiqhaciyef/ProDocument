package com.natiqhaciyef.data.mapper

import com.natiqhaciyef.common.model.Currency
import com.natiqhaciyef.common.model.Time
import com.natiqhaciyef.common.model.payment.MappedPaymentChequeModel
import com.natiqhaciyef.common.model.payment.MappedSubscriptionPlanPaymentDetails
import com.natiqhaciyef.data.network.response.PaymentChequeModel
import com.natiqhaciyef.data.network.response.SubscriptionPlanPaymentDetails

fun PaymentChequeModel.toMappedModel(): MappedPaymentChequeModel {
    return MappedPaymentChequeModel(
        checkId = this.checkId,
        title = this.title,
        description = this.description,
        subscriptionDetails = this.subscriptionDetails.toMappedModel(),
        totalAmount = this.totalAmount,
        currency = Currency.stringToType(this.currency),
        paymentDetails = this.paymentDetails
    )
}

fun MappedPaymentChequeModel.toResponse(): PaymentChequeModel {
    return PaymentChequeModel(
        checkId = this.checkId,
        title = this.title,
        description = this.description ?: "",
        subscriptionDetails = this.subscriptionDetails.toResponse(),
        totalAmount = this.totalAmount,
        currency = this.currency.name,
        paymentDetails = this.paymentDetails
    )
}

fun SubscriptionPlanPaymentDetails.toMappedModel(): MappedSubscriptionPlanPaymentDetails{
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