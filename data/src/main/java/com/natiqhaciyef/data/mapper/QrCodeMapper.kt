package com.natiqhaciyef.data.mapper

import com.natiqhaciyef.common.model.mapped.MappedQrCodePaymentModel
import com.natiqhaciyef.common.model.mapped.MappedQrCodeRequest
import com.natiqhaciyef.common.model.payment.PaymentMethods
import com.natiqhaciyef.common.model.payment.PaymentTypes
import com.natiqhaciyef.data.network.request.QrCodeRequest
import com.natiqhaciyef.data.network.response.QrPaymentResponse


fun MappedQrCodeRequest.toRequest(): QrCodeRequest{
    return QrCodeRequest(
        qrCode = this.qrCode,
        subscriptionDetails = this.subscriptionPayment.toResponse()
    )
}

fun QrCodeRequest.toMapped(): MappedQrCodeRequest{
    return MappedQrCodeRequest(
        qrCode = this.qrCode,
        subscriptionPayment = this.subscriptionDetails.toMapped()
    )
}

fun QrPaymentResponse.toMapped(): MappedQrCodePaymentModel {
    return MappedQrCodePaymentModel(
        merchantId = this.merchantId,
        paymentType = PaymentTypes.stringToType(this.paymentType),
        paymentMethod = PaymentMethods.stringToType(this.paymentMethod),
        cheque = this.cheque.toMapped()
    )
}

fun MappedQrCodePaymentModel.toResponse(): QrPaymentResponse {
    return QrPaymentResponse(
        merchantId = this.merchantId,
        paymentType = this.paymentType.name,
        paymentMethod = this.paymentMethod.name,
        cheque = this.cheque.toResponse()
    )
}