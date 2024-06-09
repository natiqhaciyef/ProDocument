package com.natiqhaciyef.common.model.payment

enum class PaymentResultType(var title: String) {
    SUCCESS("Success"),
    FAIL("Fail"),
    REVERSED("Reversed"),
    NON_STATUS("Non Status Selected");

    companion object{
        fun stringToPaymentResultType(str: String): PaymentResultType{
            return when(str.lowercase()){
                SUCCESS.title, SUCCESS.name.lowercase() -> { SUCCESS }
                FAIL.title, FAIL.name.lowercase() -> { FAIL }
                REVERSED.title, REVERSED.name.lowercase() -> { REVERSED }
                else -> { NON_STATUS }
            }
        }
    }
}