package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.common.constants.NINETY_NINE
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.model.Currency
import com.natiqhaciyef.common.model.Time
import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.common.model.payment.PaymentMethods
import com.natiqhaciyef.common.model.payment.PaymentTypes
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.request.QrCodeRequest
import com.natiqhaciyef.domain.network.response.PaymentChequeResponse
import com.natiqhaciyef.domain.network.response.QrPaymentResponse
import com.natiqhaciyef.domain.network.response.SubscriptionPlanPaymentDetails

class ScanQrCodePaymentMockGenerator(
    override var takenRequest: QrCodeRequest
) : BaseMockGenerator<QrCodeRequest, QrPaymentResponse>() {
    override var createdMock: QrPaymentResponse =
        PaymentMockManager.scanQrCodePayment(takenRequest)

    override fun getMock(
        action: ((QrCodeRequest) -> QrPaymentResponse?)?
    ): QrPaymentResponse {
        if (action != null)
            try {
                return action.invoke(takenRequest) ?: throw Companion.MockRequestException(
                    MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
                )
            } catch (e: Exception) {
                println(e)
            }

        return createdMock
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