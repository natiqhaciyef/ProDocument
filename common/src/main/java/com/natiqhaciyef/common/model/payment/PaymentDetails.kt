package com.natiqhaciyef.common.model.payment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentDetails(
    var cardHolder: String,
    var cardNumber: String,
    var expireDate: String,
    var currency: String,
    var cvv: String
): Parcelable
