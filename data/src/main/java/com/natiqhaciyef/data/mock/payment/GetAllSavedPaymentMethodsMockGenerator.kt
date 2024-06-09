package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.request.PaymentModel

class GetAllSavedPaymentMethodsMockGenerator(
    override var takenRequest: Unit
) : BaseMockGenerator<Unit, List<PaymentModel>>(){
    override var createdMock: List<PaymentModel> = PaymentMockManager.getAllPayment()

    override fun getMock(
        request: Unit,
        action: (Unit) -> List<PaymentModel>?
    ): List<PaymentModel> {
        return createdMock
    }
}