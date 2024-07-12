package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
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
        PaymentMockManager.getPayment(payment = takenRequest)

    override fun getMock(
        action: ((PaymentRequest) -> PaymentChequeResponse?)?
    ): PaymentChequeResponse {
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