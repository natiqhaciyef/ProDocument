package com.natiqhaciyef.common.helpers

import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.common.model.payment.MappedPaymentPickModel
import com.natiqhaciyef.common.model.payment.PaymentMethods


fun List<MappedPaymentModel>.toPickedModelList(): List<MappedPaymentPickModel> {
    val tempList = mutableListOf<MappedPaymentPickModel>()
    for (model in this) {
        tempList.add(
            MappedPaymentPickModel(
                type = model.paymentMethod,
                image = PaymentMethods.stringToImage(model.paymentMethod.name),
                maskedCardNumber = model.paymentDetails.cardNumber.cardMasking()
            )
        )
    }

    return tempList
}