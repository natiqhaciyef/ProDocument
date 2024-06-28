package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.common.constants.NINETY_NINE
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.model.Currency
import com.natiqhaciyef.common.model.Time
import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.common.model.payment.PaymentMethods
import com.natiqhaciyef.common.model.payment.PaymentTypes
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.request.QrCodeRequest
import com.natiqhaciyef.data.network.response.PaymentChequeResponse
import com.natiqhaciyef.data.network.response.QrPaymentResponse
import com.natiqhaciyef.data.network.response.SubscriptionPlanPaymentDetails

class ScanQrCodePaymentMockGenerator(
    override var takenRequest: QrCodeRequest
) : BaseMockGenerator<QrCodeRequest, QrPaymentResponse>() {
    override var createdMock: QrPaymentResponse =
        QrPaymentResponse(
            merchantId = NINETY_NINE,
            paymentType = PaymentTypes.QR.name,
            paymentMethod = PaymentMethods.GOOGLE_PAY.name,
            cheque = PaymentChequeResponse(
                checkId = "mock-cheque-key-indexed-1-qr",
                title = "Beginner",
                description = "Empty description",
                subscriptionDetails = SubscriptionPlanPaymentDetails(
                    expirationTimeType = Time.MONTH.title,
                    expirationTime = ONE,
                    price = 4.99,
                    fee = 0.5,
                    discount = ZERO.toDouble()
                ),
                totalAmount = 5.49,
                currency = Currency.USD.name,
                paymentDetails = PaymentDetails(
                    cardHolder = "Tahir Isazade",
                    cardNumber = "1111 2222 3333 4444",
                    expireDate = "00/00",
                    currency = Currency.USD.name,
                    cvv = "001"
                ),
                paymentResult = "SUCCESS",
                date = "00.00.0000 00:00"
            )
        )

    override fun getMock(
        request: QrCodeRequest,
        action: (QrCodeRequest) -> QrPaymentResponse?
    ): QrPaymentResponse {
        if (request == takenRequest)
            return createdMock

        return PaymentMockManager.scanQrCodePayment(takenRequest)
    }

    companion object ScanQrCodePaymentMockGenerator{
        val customRequest = QrCodeRequest(
            qrCode = "qrCode",
            subscriptionDetails = SubscriptionPlanPaymentDetails(
                expirationTimeType = Time.MONTH.title,
                expirationTime = ONE,
                price = 4.99,
                fee = 0.5,
                discount = ZERO.toDouble()
            )
        )
    }
}