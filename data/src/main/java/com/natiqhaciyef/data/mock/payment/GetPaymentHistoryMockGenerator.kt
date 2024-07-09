package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.response.PaymentChequeResponse

class GetPaymentHistoryMockGenerator(
    override var takenRequest: Unit
) : BaseMockGenerator<Unit, List<PaymentChequeResponse>>() {
    override var createdMock: List<PaymentChequeResponse> = listOf()

    override fun getMock(
        request: Unit,
        action: (Unit) -> List<PaymentChequeResponse>?
    ): List<PaymentChequeResponse> {
        return PaymentMockManager.getAllCheques()
    }
}