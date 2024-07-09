package com.natiqhaciyef.data.mock.payment


import com.natiqhaciyef.common.constants.MOCK_ERROR_OCCURRED_DUE_TO_NULL_RETURN
import com.natiqhaciyef.common.constants.TWO_HUNDRED_NINETY_NINE
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.domain.network.request.PaymentModel

class InsertNewPaymentMethodMockGenerator(
    override var takenRequest: PaymentModel
) : BaseMockGenerator<PaymentModel, CRUDResponse>() {
    override var createdMock: CRUDResponse =
        PaymentMockManager.getResult()

    override fun getMock(
        action: ((PaymentModel) -> CRUDResponse?)?
    ): CRUDResponse {
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

    companion object InsertNewPaymentMethodMockGenerator{
        val customRequest = PaymentModel(
            merchantId = ZERO,
            paymentType = "QR",
            paymentMethod = "VISA",
            paymentDetails = PaymentDetails(
                cardHolder = "Username",
                cardNumber = "0000 1111 2222 3333",
                expireDate = "12/19",
                currency = "USD",
                cvv = "909"
            )
        )
    }
}