package com.natiqhaciyef.data.mapper

import com.google.gson.Gson
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.common.model.payment.PaymentMethods
import com.natiqhaciyef.common.model.payment.PaymentTypes
import com.natiqhaciyef.data.local.entity.PaymentEntity
import com.natiqhaciyef.data.network.request.PaymentModel
import com.natiqhaciyef.data.network.request.PaymentRequest

fun PaymentModel.toMapped(): MappedPaymentModel {
    return MappedPaymentModel(
        merchantId = this.merchantId,
        paymentMethod = PaymentMethods.stringToType(this.paymentMethod),
        paymentType = PaymentTypes.stringToType(this.paymentType),
        paymentDetails = this.paymentDetails
    )
}

fun PaymentEntity.toMapped(): MappedPaymentModel{
    return MappedPaymentModel(
        merchantId = this.merchantId,
        paymentMethod = PaymentMethods.stringToType(this.paymentMethod),
        paymentType = PaymentTypes.stringToType(this.paymentType),
        paymentDetails = Gson().fromJson(this.paymentDetails, PaymentDetails::class.java)
    )
}

fun PaymentModel.toPaymentRequest(planId: String): PaymentRequest{
    return PaymentRequest(
        merchantId = this.merchantId,
        paymentType = this.paymentType,
        paymentMethod = this.paymentMethod,
        paymentDetails = this.paymentDetails,
        pickedPlanToken = planId
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

fun MappedPaymentModel.toEntity(): PaymentEntity{
    return PaymentEntity(
        id = 0,
        merchantId = this.merchantId,
        paymentType = this.paymentType.name,
        paymentMethod = this.paymentMethod.name,
        paymentDetails = Gson().toJson(this.paymentDetails, PaymentDetails::class.java)
    )
}