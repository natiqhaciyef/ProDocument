package com.natiqhaciyef.common.model.payment

import com.natiqhaciyef.common.model.payment.PaymentDetails
import com.natiqhaciyef.common.model.payment.PaymentMethods
import com.natiqhaciyef.common.model.payment.PaymentTypes

data class MappedPaymentModel(
    var merchantId: Int,
    var paymentType: PaymentTypes,
    var paymentMethod: PaymentMethods,
    var paymentDetails: PaymentDetails,
)