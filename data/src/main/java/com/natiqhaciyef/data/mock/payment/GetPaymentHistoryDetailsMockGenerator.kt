package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.common.constants.THIRTY_SIX
import com.natiqhaciyef.common.constants.TWELVE
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.PaymentChequeResponse
import com.natiqhaciyef.domain.network.response.SubscriptionPlanPaymentDetails

class GetPaymentHistoryDetailsMockGenerator(
    override var takenRequest: String
) : BaseMockGenerator<String, PaymentChequeResponse>() {
    override var createdMock: PaymentChequeResponse =
        PaymentChequeResponse(
            checkId = "mock-key-id",
            title = "Payment for plan",
            description = "Payment refund is not available",
            subscriptionDetails = SubscriptionPlanPaymentDetails(
                expirationTime = TWELVE,
                expirationTimeType = "Month",
                price = THIRTY_SIX.toDouble(),
                fee = 2.99,
                discount = ZERO.toDouble()
            ),
            totalAmount = 38.99,
            currency = "USD",
            paymentDetails = PaymentDetails(
                cardHolder = "Mock cardholder",
                cardNumber = "0000 1111 2222 3333",
                expireDate = "00/00",
                currency = "USD",
                cvv = "000"
            ),
            paymentResult = "SUCCESS",
            date = "00.00.0000 00:00"
        )

    override fun getMock(
        request: String,
        action: (String) -> PaymentChequeResponse?
    ): PaymentChequeResponse {
        if (request == takenRequest)
            return createdMock

        return PaymentMockManager.getChequeDetails(takenRequest)
    }

    companion object GetPaymentHistoryDetailsMockGenerator{
        const val customRequest = "customChequeId"
    }
}