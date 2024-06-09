package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.common.R
import com.natiqhaciyef.data.network.request.PaymentModel
import com.natiqhaciyef.data.network.response.PaymentPickModel

class GetPickedPaymentDetailsMockGenerator(
    override var takenRequest: PaymentPickModel
) : BaseMockGenerator<PaymentPickModel, PaymentModel>() {
    override var createdMock: PaymentModel = PaymentModel(
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

    override fun getMock(
        request: PaymentPickModel,
        action: (PaymentPickModel) -> PaymentModel?
    ): PaymentModel {
        if (request == takenRequest)
            return createdMock

        return PaymentMockManager.getPickedPaymentDetails(takenRequest) ?: createdMock
    }

    companion object GetPickedPaymentDetails{
        val customRequest = PaymentPickModel(type = "Visa", image = R.drawable.visa)
    }
}