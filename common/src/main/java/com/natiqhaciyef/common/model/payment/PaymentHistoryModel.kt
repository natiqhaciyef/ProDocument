package com.natiqhaciyef.common.model.payment

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.natiqhaciyef.common.model.Currency
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentHistoryModel(
    var chequeId: String,
    @DrawableRes var icon: Int,
    var title: String,
    var maskedCardNumber: String,
    var price: Double,
    var currency: Currency,
): Parcelable
