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
    UNKNOWN(R.string.other);

    companion object{

        fun stringToImage(title: String): Int{
            return when(title.lowercase()) {
                VISA.name.lowercase() -> { R.drawable.visa }
                MASTERCARD.name.lowercase() -> { R.drawable.mastercard }
                AMERICAN_EXPRESS.name.lowercase() -> { R.drawable.american_express }
                PAYPAL.name.lowercase() -> { R.drawable.paypal }
                GOOGLE_PAY.name.lowercase() -> { R.drawable.google_pay }
                APPLE_PAY.name.lowercase() -> { R.drawable.apple_pay }
                else -> R.drawable.profile_account_icon
            }
        }

        fun stringToType(str: String): PaymentMethods{
            return when(str.lowercase()) {
                VISA.name.lowercase() -> { VISA }
                MASTERCARD.name.lowercase() -> { MASTERCARD }
                AMERICAN_EXPRESS.name.lowercase() -> { AMERICAN_EXPRESS }
                PAYPAL.name.lowercase() -> { PAYPAL }
                GOOGLE_PAY.name.lowercase() -> { GOOGLE_PAY }
                APPLE_PAY.name.lowercase() -> { APPLE_PAY }
                else -> UNKNOWN
            }
        }

    }
}