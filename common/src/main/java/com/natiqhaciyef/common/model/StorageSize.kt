package com.natiqhaciyef.common.model

enum class StorageSize(val size: Int, val type: StorageSize) {
    Bite(1, Bite),
    Byte(8, Bite),
    KB(1024, Byte),
    MB(1024, KB),
    GB(1024, MB),
    TB(1024, GB);

    companion object {
        fun stringToStorageSize(str: String) =
            when(str.lowercase()){
                Bite.name.lowercase() -> { Bite }
                Byte.name.lowercase() -> { Byte }
                KB.name.lowercase() -> { KB }
                MB.name.lowercase() -> { MB }
                GB.name.lowercase() -> { GB }
                TB.name.lowercase() -> { TB }
                else -> MB
            }
    }
}