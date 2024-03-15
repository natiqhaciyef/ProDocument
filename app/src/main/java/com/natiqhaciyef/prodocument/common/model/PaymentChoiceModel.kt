package com.natiqhaciyef.prodocument.common.model

data class PaymentChoiceModel(
    var type: PaymentTypes,
    var image: Int,
    var isSelected: Boolean,
)