package com.natiqhaciyef.data.mapper

import com.natiqhaciyef.common.model.mapped.MappedPaymentModel
import com.natiqhaciyef.common.model.payment.PaymentMethods
import com.natiqhaciyef.common.model.payment.PaymentTypes
import com.natiqhaciyef.data.network.request.PaymentModel

fun PaymentModel.toMapped(): MappedPaymentModel{
    return MappedPaymentModel(
        merchantId = this.merchantId,
        paymentMethod = PaymentMethods.stringToType(this.paymentMethod),
        paymentType = PaymentTypes.stringToType(this.paymentType),
        paymentDetails = this.paymentDetails
    )
}

fun MappedPaymentModel.toResponse(): PaymentModel{
    return PaymentModel(
        merchantId = this.merchantId,
        paymentDetails = this.paymentDetails,
        paymentType = this.paymentType.name.lowercase(),
        paymentMethod = this.paymentMethod.name.lowercase()
    )
}