package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.domain.network.request.PaymentModel
import com.natiqhaciyef.domain.network.response.PaymentPickModel

class GetPickedPaymentDetailsMockGenerator(
    override var takenRequest: PaymentPickModel
) : BaseMockGenerator<PaymentPickModel, PaymentModel>() {
    override var createdMock: PaymentModel =
        PaymentMockManager.getPickedPaymentDetails(takenRequest)

    override fun getMock(
        action: ((PaymentPickModel) -> PaymentModel?)?
    ): PaymentModel {
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

    companion object GetPickedPaymentDetails{
        val customRequest = PaymentPickModel(type = "Visa", image = R.drawable.visa)
    }
}