package com.natiqhaciyef.domain.network.request

import com.natiqhaciyef.domain.network.response.MaterialResponse


data class ESignRequest(
    val sign: String,
    val signBitmap: String,
    val material: MaterialResponse,
    val page: Int,
    val positionX: Float,
    val positionY: Float
)
