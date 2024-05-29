package com.natiqhaciyef.common.model.payment

data class PaymentDetails(
    var username: String,
    var cardNumber: String,
    var expireDate: String,
    var cvv: Int
)
