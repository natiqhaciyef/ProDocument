package com.natiqhaciyef.common.helpers

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.TextView
import com.natiqhaciyef.common.R
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.FIFTEEN
import com.natiqhaciyef.common.constants.FIVE
import com.natiqhaciyef.common.constants.FOUR
import com.natiqhaciyef.common.constants.NINETEEN
import com.natiqhaciyef.common.constants.ONE
import com.natiqhaciyef.common.constants.SIXTEEN
import com.natiqhaciyef.common.constants.SLASH
import com.natiqhaciyef.common.constants.SPACE
import com.natiqhaciyef.common.constants.THREE
import com.natiqhaciyef.common.constants.TWO
import com.natiqhaciyef.common.constants.ZERO
import com.natiqhaciyef.common.model.payment.MappedPaymentPickModel
import com.natiqhaciyef.common.model.payment.PaymentMethods


class CardNumberMaskingListener(val view: TextView) : TextWatcher {
    private var current = EMPTY_STRING

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(s: Editable) {
        if (s.toString() != current) {
            val userInput = s.toString().replace(nonDigits, EMPTY_STRING)
            if (userInput.length <= SIXTEEN) {
                current = userInput.chunked(FOUR).joinToString(SPACE)
                s.filters = arrayOfNulls<InputFilter>(ZERO)
            }
            s.replace(ZERO, s.length, current, ZERO, current.length)
            view.text = s
        }
    }

    companion object {
        private val nonDigits = Regex("[^\\d]")
    }
}

class ExpireMaskingListener(val view: TextView) : TextWatcher {
    private var current = EMPTY_STRING

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(s: Editable) {
        if (s.toString() != current) {
            val userInput = s.toString().replace(nonDigits, EMPTY_STRING)
            if (userInput.length <= FOUR) {
                current = userInput.chunked(TWO).joinToString(SLASH)
                s.filters = arrayOfNulls<InputFilter>(ZERO)

            }
            s.replace(ZERO, s.length, current, ZERO, current.length)
            view.text = s
        }
    }

    companion object {
        private val nonDigits = Regex("[^\\d]")
    }
}


fun formatOtherCardNumbers(text: String): String {
    if (text.onlyDigits()) {
        val trimmed = if (text.length >= SIXTEEN) text.substring(ZERO until SIXTEEN) else text
        var out = EMPTY_STRING

        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % FOUR == THREE && i != FIFTEEN) out += SPACE
        }
        return out
    }
    return text
}

fun formatExpirationDate(text: String): String {
    if (text.onlyDigits()) {
        val trimmed = if (text.length >= FIVE) text.substring(ZERO..THREE) else text
        var out = EMPTY_STRING

        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % THREE == ONE) out += SLASH
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
    return if (this.trim().length == NINETEEN) {
        val result = this.toCharArray().toMutableList()
        for (i in this.indices) {
            if (this[i].isDigit() && i < FIFTEEN) {
                result[i] = '*'
            }
        }

        result.toDesignedString()
    } else {
        this
    }
}

fun MutableList<Char>.toDesignedString(): String {
    var result = EMPTY_STRING
    for (i in this) {
        result += i
    }
    return result
}