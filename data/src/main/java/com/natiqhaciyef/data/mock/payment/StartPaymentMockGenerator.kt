package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.common.constants.TWELVE
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.response.PaymentChequeResponse
import com.natiqhaciyef.data.network.response.SubscriptionPlanPaymentDetails

class StartPaymentMockGenerator(
    override var takenRequest: PaymentChequeResponse
) : BaseMockGenerator<PaymentChequeResponse, CRUDResponse>() {
    override var createdMock: CRUDResponse =
        CRUDResponse(
            resultCode = TWO_HUNDRED_NINETY_NINE,
            message = "mock result success"
        )

    override fun getMock(
        request: PaymentChequeResponse,
        action: (PaymentChequeResponse) -> CRUDResponse?
    ): CRUDResponse {
        return PaymentMockManager.startPayment(takenRequest)
    }

    companion object StartPaymentMockGenerator {
        private val chequeList = PaymentMockManager.getAllCheques()
        val customRequest = if (chequeList.isNotEmpty())
            chequeList.first()
        else
            PaymentChequeResponse(
                checkId = "mock-key-id",
                title = "Payment for plan",
                description = "Payment refund is not available",
                subscriptionDetails = SubscriptionPlanPaymentDetails(
                    expirationTime = TWELVE,
                    expirationTimeType = "Month",
                    price = 36.00,
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
    }
}