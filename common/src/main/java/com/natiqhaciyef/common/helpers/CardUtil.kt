package com.natiqhaciyef.common.helpers

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.TextView
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.model.payment.MappedPaymentPickModel
import com.natiqhaciyef.common.model.payment.PaymentMethods


class CardNumberMaskingListener(val view: TextView) : TextWatcher {
    private var current = ""

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(s: Editable) {
        if (s.toString() != current) {
            val userInput = s.toString().replace(nonDigits, "")
            if (userInput.length <= 16) {
                current = userInput.chunked(4).joinToString(" ")
                s.filters = arrayOfNulls<InputFilter>(0)
            }
            s.replace(0, s.length, current, 0, current.length)
            view.text = s
        }
    }

    companion object {
        private val nonDigits = Regex("[^\\d]")
    }
}

class ExpireMaskingListener(val view: TextView) : TextWatcher {
    private var current = ""

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(s: Editable) {
        if (s.toString() != current) {
            val userInput = s.toString().replace(nonDigits, "")
            if (userInput.length <= 4) {
                current = userInput.chunked(2).joinToString("/")
                s.filters = arrayOfNulls<InputFilter>(0)

            }
            s.replace(0, s.length, current, 0, current.length)
            view.text = s
        }
    }

    companion object {
        private val nonDigits = Regex("[^\\d]")
    }
}


fun formatOtherCardNumbers(text: String): String {
    if (text.onlyDigits()) {
        val trimmed = if (text.length >= 16) text.substring(0..15) else text
        var out = ""

        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % 4 == 3 && i != 15) out += " "
        }
        return out
    }
    return text
}

fun formatExpirationDate(text: String): String {
    if (text.onlyDigits()) {
        val trimmed = if (text.length >= 5) text.substring(0..3) else text
        var out = ""

        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % 3 == 1) out += "/"
        }
        return out
    }
    return text
}


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
        PaymentMethods.VISA -> {
            R.drawable.visa
        }

        PaymentMethods.MASTERCARD -> {
            R.drawable.mastercard
        }

        PaymentMethods.PAYPAL -> {
            R.drawable.paypal
        }

        PaymentMethods.AMERICAN_EXPRESS -> {
            R.drawable.american_express
        }

        PaymentMethods.GOOGLE_PAY -> {
            R.drawable.google_pay
        }

        PaymentMethods.APPLE_PAY -> {
            R.drawable.apple_pay
        }

        else -> 0
    }
}

fun String.cardMasking(): String {
    return if (this.trim().length == 19) {
        val result = this.toCharArray().toMutableList()
        for (i in this.indices) {
            if (this[i].isDigit() && i < 15) {
                result[i] = '*'
            }
        }

        result.toDesignedString()
    } else {
        this
    }
}

fun MutableList<Char>.toDesignedString(): String {
    var result = ""
    for (i in this) {
        result += i
    }
    return result
}