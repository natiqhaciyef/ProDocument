package com.natiqhaciyef.common.model.payment

import androidx.annotation.StringRes
import com.natiqhaciyef.common.R

enum class PaymentMethods(@StringRes var resourceId: Int) {
    VISA(R.string.visa),
    MASTERCARD(R.string.mastercard),
    AMERICAN_EXPRESS(R.string.american_express),
    PAYPAL(R.string.paypal),
    GOOGLE_PAY(R.string.google_pay),
    APPLE_PAY(R.string.apple_pay),
    UNKNOWN(R.string.other)
}