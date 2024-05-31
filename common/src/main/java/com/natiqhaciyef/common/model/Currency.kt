package com.natiqhaciyef.common.model

enum class Currency {
    AZN,
    USD,
    EUR,
    CAD,
    TL,
    RUB;

    companion object{
        fun stringToType(str: String): Currency{
            return when(str.lowercase()){
                AZN.name.lowercase() -> { AZN }
                USD.name.lowercase() -> { USD }
                EUR.name.lowercase() -> { EUR }
                CAD.name.lowercase() -> { CAD }
                TL.name.lowercase() -> { TL }
                RUB.name.lowercase() -> { RUB }
                else -> USD
            }
        }
    }
}