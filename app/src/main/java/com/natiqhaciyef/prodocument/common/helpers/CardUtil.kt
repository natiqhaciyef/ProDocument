package com.natiqhaciyef.prodocument.common.helpers

import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.common.clazz.PaymentChoiceModel
import com.natiqhaciyef.prodocument.common.clazz.PaymentTypes


fun String.onlyDigits(): Boolean{
    return this.all { it.isDigit() }
}

object PaymentMethodList {
    val list = mutableListOf(
        PaymentChoiceModel(
            type = PaymentTypes.VISA,
            image = R.drawable.visa,
            isSelected = true
        ),
        PaymentChoiceModel(
            type = PaymentTypes.MASTERCARD,
            image = R.drawable.mastercard,
            isSelected = false
        ),
        PaymentChoiceModel(
            type = PaymentTypes.PAYPAL,
            image = R.drawable.paypal,
            isSelected = false
        ),
    )
}

fun cardTypeToImageFinder(paymentMethod: String): Int {
    return when (paymentMethod.lowercase()) {
        PaymentTypes.VISA.mainName.lowercase() -> PaymentMethodList.list[0].image
        PaymentTypes.MASTERCARD.mainName.lowercase() -> PaymentMethodList.list[1].image
        PaymentTypes.PAYPAL.mainName.lowercase() -> PaymentMethodList.list[2].image
        else -> 0
    }
}

