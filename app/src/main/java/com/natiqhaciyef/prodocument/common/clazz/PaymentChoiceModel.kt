package com.natiqhaciyef.prodocument.common.clazz

data class PaymentChoiceModel(
    var type: PaymentTypes,
    var image: Int,
    var isSelected: Boolean,
)