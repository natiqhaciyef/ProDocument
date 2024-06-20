package com.natiqhaciyef.common.helpers

import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.model.mapped.MappedSubscriptionModel
import com.natiqhaciyef.common.model.payment.MappedPaymentModel
import com.natiqhaciyef.common.model.payment.MappedPaymentPickModel
import com.natiqhaciyef.common.model.payment.MappedSubscriptionPlanPaymentDetails
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

fun MappedSubscriptionModel.toDetails(): MappedSubscriptionPlanPaymentDetails {
    return MappedSubscriptionPlanPaymentDetails(
        expirationTime = this.perTime,
        expirationTimeType = this.timeType,
        price = this.price,
        fee = this.price * 0.1,
        discount = ZERO.toDouble()
    )
}