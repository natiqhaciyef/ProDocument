package com.natiqhaciyef.domain.mapper

import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.common.model.payment.PaymentMethods
import com.natiqhaciyef.common.model.payment.PaymentTypes
import com.natiqhaciyef.domain.network.request.PaymentModel
import com.natiqhaciyef.domain.network.request.PaymentRequest

fun PaymentModel.toMapped(): MappedPaymentModel {
    return MappedPaymentModel(
        merchantId = this.merchantId,
        paymentMethod = PaymentMethods.stringToType(this.paymentMethod),
        paymentType = PaymentTypes.stringToType(this.paymentType),
        paymentDetails = this.paymentDetails
    )
}

fun PaymentModel.toPaymentRequest(planId: String): PaymentRequest {
    return PaymentRequest(
        merchantId = this.merchantId,
        paymentType = this.paymentType,
        paymentMethod = this.paymentMethod,
        paymentDetails = this.paymentDetails,
        pickedPlanToken = planId
    )
}

fun MappedPaymentModel.toResponse(): PaymentModel {
    return PaymentModel(
        merchantId = this.merchantId,
        paymentDetails = this.paymentDetails,
        paymentType = this.paymentType.name.lowercase(),
        paymentMethod = this.paymentMethod.name.lowercase()
    )
}