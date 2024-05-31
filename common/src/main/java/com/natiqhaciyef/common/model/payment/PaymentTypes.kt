package com.natiqhaciyef.common.model.payment

enum class PaymentTypes{
    QR,
    B2B,
    CARD,
    PROMO_CODE;

    companion object{
        fun stringToType(str: String): PaymentTypes{
            return when(str.lowercase()){
                QR.name.lowercase() -> { QR }
                B2B.name.lowercase() -> { B2B }
                CARD.name.lowercase() -> { CARD }
                PROMO_CODE.name.lowercase() -> { PROMO_CODE }
                else -> CARD
            }
        }
    }
}
