package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.data.network.request.PaymentModel
import com.natiqhaciyef.data.network.response.PaymentChequeModel
import com.natiqhaciyef.data.network.response.SubscriptionPlanPaymentDetails

class StartPaymentMockGenerator(
    override var takenRequest: PaymentModel
) : BaseMockGenerator<PaymentModel, PaymentChequeModel>() {
    override var createdMock: PaymentChequeModel =
        PaymentChequeModel(
            checkId = "mock-key-id",
            title = "Payment food",
            description = "Payment refund is not available",
            subscriptionDetails = SubscriptionPlanPaymentDetails(
                expirationTime = 12,
                expirationTimeType = "Month",
                price = 36.0,
                fee = 2.26,
                discount = 0.0
            ),
            totalAmount = 38.26,
            currency = "USD",
            paymentDetails = PaymentDetails(
                cardHolder = "Username",
                cardNumber = "0000 1111 2222 3333",
                expireDate = "12/19",
                currency = "USD",
                cvv = "909"
            )
        )

    override fun getMock(
        request: PaymentModel,
        action: (PaymentModel) -> PaymentChequeModel?
    ): PaymentChequeModel {

        return if(request.paymentDetails == takenRequest.paymentDetails)
            createdMock
        else
            action.invoke(takenRequest) ?: throw Companion.MockRequestException()
    }

    companion object StartPaymentMockGenerator{
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