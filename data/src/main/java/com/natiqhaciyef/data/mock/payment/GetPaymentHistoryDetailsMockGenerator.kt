package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
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
        PaymentMockManager.getChequeDetails(takenRequest)

    override fun getMock(
        action: ((String) -> PaymentChequeResponse?)?
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

    companion object GetPaymentHistoryDetailsMockGenerator{
        const val customRequest = "customChequeId"
    }
}