package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.common.R
import com.natiqhaciyef.data.network.response.PaymentPickModel

class GetAllSavedPaymentMethodsMockGenerator(
    override var takenRequest: Unit
) : BaseMockGenerator<Unit, List<PaymentPickModel>>(){
    override var createdMock: List<PaymentPickModel> =
        listOf(
            PaymentPickModel(type = "Visa", image = R.drawable.visa),
            PaymentPickModel(type = "Mastercard", image = R.drawable.mastercard),
            PaymentPickModel(type = "Google pay", image = R.drawable.google_pay)
        )

    override fun getMock(
        request: Unit,
        action: (Unit) -> List<PaymentPickModel>?
    ): List<PaymentPickModel> {
        return createdMock
    }
}