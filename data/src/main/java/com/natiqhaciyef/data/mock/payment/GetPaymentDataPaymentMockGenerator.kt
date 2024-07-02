package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.common.constants.THIRTY_SIX
import com.natiqhaciyef.common.constants.TWELVE
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.request.PaymentRequest
import com.natiqhaciyef.domain.network.response.PaymentChequeResponse
import com.natiqhaciyef.domain.network.response.SubscriptionPlanPaymentDetails

class GetPaymentDataPaymentMockGenerator(
    override var takenRequest: PaymentRequest
) : BaseMockGenerator<PaymentRequest, PaymentChequeResponse>() {
    override var createdMock: PaymentChequeResponse =
        PaymentChequeResponse(
            checkId = "mock-key-id",
            title = "Payment food",
            description = "Payment refund is not available",
            subscriptionDetails = SubscriptionPlanPaymentDetails(
                expirationTime = TWELVE,
                expirationTimeType = "Month",
                price = THIRTY_SIX.toDouble(),
                fee = 2.26,
                discount = ZERO.toDouble()
            ),
            totalAmount = 38.26,
            currency = "USD",
            paymentDetails = PaymentDetails(
                cardHolder = "Username",
                cardNumber = "0000 1111 2222 3333",
                expireDate = "12/19",
                currency = "USD",
                cvv = "909"
            ),
            paymentResult = "SUCCESS",
            date = "00.00.0000 00:00"
        )

    override fun getMock(
        request: PaymentRequest,
        action: (PaymentRequest) -> PaymentChequeResponse?
    ): PaymentChequeResponse {
        if (request == takenRequest)
            return createdMock

        return PaymentMockManager.getPayment(payment = takenRequest)
    }

    companion object StartPaymentMockGenerator{
        val customRequest = PaymentRequest(
            merchantId = ZERO,
            paymentType = "QR",
            paymentMethod = "VISA",
            paymentDetails = PaymentDetails(
                cardHolder = "Username",
                cardNumber = "0000 1111 2222 3333",
                expireDate = "12/19",
                currency = "USD",
                cvv = "909"
            ),
            pickedPlanToken = "subscriptionToken-3"
        )
    }
}