package com.natiqhaciyef.common.helpers

import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.model.payment.MappedPaymentPickModel
import com.natiqhaciyef.common.model.payment.PaymentMethods


fun String.onlyDigits(): Boolean {
    return this.all { it.isDigit() }
}

object PaymentMethodList {
    val list = mutableListOf(
        MappedPaymentPickModel(
            type = PaymentMethods.VISA,
            image = R.drawable.visa,
        ),
        MappedPaymentPickModel(
            type = PaymentMethods.MASTERCARD,
            image = R.drawable.mastercard,
        ),
        MappedPaymentPickModel(
            type = PaymentMethods.PAYPAL,
            image = R.drawable.paypal,
        ),
        MappedPaymentPickModel(
            type = PaymentMethods.GOOGLE_PAY,
            image = R.drawable.google_pay
        ),
        MappedPaymentPickModel(
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

fun String.cardMasking(): String{
    return if (this.trim().length == 19){
        val result = this.toCharArray().toMutableList()
        for (i in this.indices){
            if (this[i].isDigit() && i < 15){
                result[i] = '*'
            }
        }

        result.toDesignedString()
    }else{
        this
    }
}

fun MutableList<Char>.toDesignedString(): String{
    var result = ""
    for (i in this){
        result += i
    }
    return result
}