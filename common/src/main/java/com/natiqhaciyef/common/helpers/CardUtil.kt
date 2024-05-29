package com.natiqhaciyef.common.helpers

import androidx.annotation.DrawableRes
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.model.payment.PaymentPickModel
import com.natiqhaciyef.common.model.payment.PaymentMethods


fun String.onlyDigits(): Boolean {
    return this.all { it.isDigit() }
}

object PaymentMethodList {
    val list = mutableListOf(
        PaymentPickModel(
            type = PaymentMethods.VISA,
            image = R.drawable.visa,
        ),
        PaymentPickModel(
            type = PaymentMethods.MASTERCARD,
            image = R.drawable.mastercard,
        ),
        PaymentPickModel(
            type = PaymentMethods.PAYPAL,
            image = R.drawable.paypal,
        ),
        PaymentPickModel(
            type = PaymentMethods.GOOGLE_PAY,
            image = R.drawable.google_pay
        ),
        PaymentPickModel(
            type = PaymentMethods.APPLE_PAY,
            image = R.drawable.apple_pay
        )
    )
}

fun cardTypeToImageFinder(paymentMethod: PaymentMethods): Int {
    return when (paymentMethod) {
        PaymentMethods.VISA -> { R.drawable.visa }
        PaymentMethods.MASTERCARD -> { R.drawable.mastercard }
        PaymentMethods.PAYPAL -> { R.drawable.paypal }
        PaymentMethods.AMERICAN_EXPRESS -> { R.drawable.american_express }
        PaymentMethods.GOOGLE_PAY -> { R.drawable.google_pay }
        PaymentMethods.APPLE_PAY -> { R.drawable.apple_pay }
        else -> 0
    }
}

