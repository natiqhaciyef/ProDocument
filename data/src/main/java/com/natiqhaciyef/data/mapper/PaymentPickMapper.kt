package com.natiqhaciyef.data.mapper

import com.natiqhaciyef.common.helpers.cardMasking
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.common.model.payment.MappedPaymentPickModel
import com.natiqhaciyef.common.model.payment.PaymentMethods
import com.natiqhaciyef.data.network.response.PaymentPickModel

fun PaymentPickModel.toMapped(): MappedPaymentPickModel{
    return MappedPaymentPickModel(
        type = PaymentMethods.stringToType(this.type),
        image = this.image,
        maskedCardNumber = this.maskedCardNumber
    )
}

fun MappedPaymentPickModel.toResponse(): PaymentPickModel{
    return PaymentPickModel(
        type = this.type.name,
        image = this.image,
        maskedCardNumber = this.maskedCardNumber
    )
}