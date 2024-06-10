package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.common.helpers.getNow
import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.response.PaymentChequeResponse
import com.natiqhaciyef.data.network.response.SubscriptionPlanPaymentDetails

class InsertChequeForPaymentHistoryMockGenerator(
    override var takenRequest: PaymentChequeResponse
) : BaseMockGenerator<PaymentChequeResponse, CRUDResponse>() {
    override var createdMock: CRUDResponse =
        CRUDResponse(
            resultCode = 299,
            message = "insert cheque mock result"
        )

    override fun getMock(
        request: PaymentChequeResponse,
        action: (PaymentChequeResponse) -> CRUDResponse?
    ): CRUDResponse {
        if (request == takenRequest)
            return createdMock

        return PaymentMockManager.insertCheque(takenRequest)
    }

    companion object InsertChequeForPaymentHistoryMockGenerator{
        val customRequest = PaymentChequeResponse(
            checkId = "mock-key-id",
            title = "Payment for plan",
            description = "Payment refund is not available",
            subscriptionDetails = SubscriptionPlanPaymentDetails(
                expirationTime = 12,
                expirationTimeType = "Month",
                price = 36.00,
                fee = 2.99,
                discount = 0.0
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

    }
}