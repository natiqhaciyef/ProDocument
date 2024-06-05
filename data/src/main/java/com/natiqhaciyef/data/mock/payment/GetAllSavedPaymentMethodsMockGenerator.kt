package com.natiqhaciyef.data.mock.payment

import com.natiqhaciyef.core.base.mock.BaseMockGenerator
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.common.model.payment.PaymentMethods
import com.natiqhaciyef.common.model.payment.PaymentTypes
import com.natiqhaciyef.data.network.request.PaymentModel
import com.natiqhaciyef.data.network.response.PaymentPickModel

class GetAllSavedPaymentMethodsMockGenerator(
    override var takenRequest: Unit
) : BaseMockGenerator<Unit, List<PaymentModel>>(){
    override var createdMock: List<PaymentModel> =
        listOf(
            PaymentModel(
                merchantId = 100,
                paymentType = PaymentTypes.CARD.name,
                paymentMethod = PaymentMethods.MASTERCARD.name,
                paymentDetails = PaymentDetails(
                    cardHolder = "Anyone",
                    cardNumber = "0000 1111 2222 3333",
                    expireDate = "00/00",
                    currency = "USD",
                    cvv = "000"
                )
            ),
            PaymentModel(
                merchantId = 101,
                paymentType = PaymentTypes.CARD.name,
                paymentMethod = PaymentMethods.VISA.name,
                paymentDetails = PaymentDetails(
                    cardHolder = "No one",
                    cardNumber = "0000 2222 4444 6666",
                    expireDate = "00/00",
                    currency = "USD",
                    cvv = "000"
                )
            )
        )

    override fun getMock(
        request: Unit,
        action: (Unit) -> List<PaymentModel>?
    ): List<PaymentModel> {
        return createdMock
    }
}