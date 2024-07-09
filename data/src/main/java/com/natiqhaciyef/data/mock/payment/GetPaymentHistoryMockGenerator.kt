package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.PaymentChequeResponse

class GetPaymentHistoryMockGenerator(
    override var takenRequest: Unit
) : BaseMockGenerator<Unit, List<PaymentChequeResponse>>() {
    override var createdMock: List<PaymentChequeResponse> =
        PaymentMockManager.getAllCheques()

    override fun getMock(
        action: ((Unit) -> List<PaymentChequeResponse>?)?
    ): List<PaymentChequeResponse> {
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
}