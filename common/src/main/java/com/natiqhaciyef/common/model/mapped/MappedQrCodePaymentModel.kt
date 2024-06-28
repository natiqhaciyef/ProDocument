package com.natiqhaciyef.common.model.mapped

import android.os.Parcelable
import com.natiqhaciyef.common.model.payment.MappedPaymentChequeModel
import com.natiqhaciyef.common.model.payment.PaymentMethods
import com.natiqhaciyef.common.model.payment.PaymentTypes
import kotlinx.parcelize.Parcelize

@Parcelize
data class MappedQrCodePaymentModel(
    var merchantId: Int,
    var paymentType: PaymentTypes,
    var paymentMethod: PaymentMethods,
    var cheque: MappedPaymentChequeModel
) : Parcelable

