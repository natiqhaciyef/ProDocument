package com.natiqhaciyef.common.model.payment

import android.os.Parcelable
import com.natiqhaciyef.common.helpers.cardMasking
import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.common.model.payment.PaymentMethods
import com.natiqhaciyef.common.model.payment.PaymentTypes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MappedPaymentModel(
    var merchantId: Int,
    var paymentType: PaymentTypes,
    var paymentMethod: PaymentMethods,
    var paymentDetails: PaymentDetails,
): Parcelable {
    companion object{
        fun MappedPaymentModel.toMappedPick(): MappedPaymentPickModel{
            return MappedPaymentPickModel(
                type = this.paymentMethod,
                image = PaymentMethods.stringToImage(this.paymentMethod.name),
                maskedCardNumber = this.paymentDetails.cardNumber.cardMasking()
            )
        }
    }
}