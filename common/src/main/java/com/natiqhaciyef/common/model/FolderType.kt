package com.natiqhaciyef.common.model

enum class FolderType {
    MIX,
    IMAGES,
    FILES,
    CUSTOM;

    companion object {
        fun stringToFolderType(type: String) =
            when (type.lowercase()) {
                MIX.name, MIX.name.lowercase() -> MIX
                IMAGES.name, IMAGES.name.lowercase() -> IMAGES
                FILES.name, FILES.name.lowercase() -> FILES
                CUSTOM.name, CUSTOM.name.lowercase() -> CUSTOM
                else -> CUSTOM
            }
    }
}