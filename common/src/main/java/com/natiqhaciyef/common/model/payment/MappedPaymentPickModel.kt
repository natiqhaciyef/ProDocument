package com.natiqhaciyef.common.model.payment

import com.natiqhaciyef.common.constants.EMPTY_STRING

data class MappedPaymentPickModel(
    var type: PaymentMethods,
    var image: Int,
    var maskedCardNumber: String = EMPTY_STRING,
)