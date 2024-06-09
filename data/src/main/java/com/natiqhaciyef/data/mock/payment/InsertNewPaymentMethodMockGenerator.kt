package com.natiqhaciyef.data.mock.payment


import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.core.CRUDResponse
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.request.PaymentModel

class InsertNewPaymentMethodMockGenerator(
    override var takenRequest: PaymentModel
) : BaseMockGenerator<PaymentModel, CRUDResponse>() {
    override var createdMock: CRUDResponse = CRUDResponse(
        resultCode = 299,
        message = "mock result success"
    )


    override fun getMock(
        request: PaymentModel,
        action: (PaymentModel) -> CRUDResponse?
    ): CRUDResponse {
        if (request == takenRequest)
            return createdMock

        return PaymentMockManager.insertNewPayment(takenRequest)
    }

    companion object InsertNewPaymentMethodMockGenerator{
        val customRequest = PaymentModel(
            merchantId = 0,
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